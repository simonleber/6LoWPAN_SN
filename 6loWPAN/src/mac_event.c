/* Copyright (c) 2008  ATMEL Corporation
   All rights reserved.

   Redistribution and use in source and binary forms, with or without
   modification, are permitted provided that the following conditions are met:

 * Redistributions of source code must retain the above copyright
     notice, this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright
     notice, this list of conditions and the following disclaimer in
     the documentation and/or other materials provided with the
     distribution.
 * Neither the name of the copyright holders nor the names of
     contributors may be used to endorse or promote products derived
     from this software without specific prior written permission.

  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
  AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
  IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
  ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
  LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
  CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
  SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
  INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
  CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
  ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
  POSSIBILITY OF SUCH DAMAGE.
 */
/*
  $Id: mac_event.c,v 1.1.2.1 2010/09/16 08:21:10 ele Exp $
 */

// Includes
#include <stdlib.h>
#include <stdio.h>
#include "../inc/mac.h"
#include "../inc/mac_event.h"
#include "../inc/mac_data.h"
#include "../inc/mac_scan.h"
#include "../inc/mac_associate.h"
#include "../inc/radio.h"
#include "../inc/system.h"
#include "../inc/avr_timer.h"

#include "../inc/deRFaddon/bmm.h"
#include "../inc/deRFaddon/uart.h"


/**
   @ingroup mac
   @{
   @defgroup mac_event MAC event handler
   @{

   This module processes events generated by the MAC.  Events include
   interrupts from the radio chip, and expired timer events.  The
   macTask() function should be called periodically from the main loop
   or OS process to process these events. @see mac_timer
 */

// Globals

/// Number of events in event queue
#define MAX_EVENTS 20

/// Structure that defines the event queue.
typedef struct {
   volatile u8 head;   ///< The index of the last available event
   volatile u8 tail;   ///< The index of the first available event
   event_object_t event_object[MAX_EVENTS]; ///< List of events in queue
} event_queue_t;
event_queue_t event_queue; ///< The event queue itself

// Implementation

/**
    @brief Checks for any pending events in the queue.

    @return True if there is a pending event, else false.
 */
u8 mac_event_pending(void)
{
   return (event_queue.head != event_queue.tail);
}


/**
    @brief Puts an event into the queue of events.

    @param object is a pointer to the event to add to queue.
 */
void mac_put_event(event_object_t *object)
{
   AVR_ENTER_CRITICAL_REGION();
   u8 newhead;

   if ((event_queue.head + 1) % MAX_EVENTS == event_queue.tail)
   {
      if(object->data != 0)
      {
         bmm_buffer_free(object->data);
      }
      // queue full, get outta here
      return;
   }

   newhead = event_queue.head;

   // store in queue
   event_queue.event_object[newhead] = *object;

   // calculate new head index
   newhead++;
   if (newhead >= MAX_EVENTS)
      newhead = 0;
   event_queue.head = newhead;
   AVR_LEAVE_CRITICAL_REGION();
}

/**
    @brief Pulls an event from the event queue.  Assumes that there is
    an event in the queue.  Be sure that mac_event_pending() returns
    true before calling this function.

    @return Pointer to the event object, or garbage data if the queue
    is empty.
 */
event_object_t * mac_get_event(void)
{
   event_object_t *object = NULL;
   AVR_ENTER_CRITICAL_REGION();
   volatile u8 newtail;

   newtail = event_queue.tail;

   object = &(event_queue.event_object[newtail]);

   // calculate new tail
   newtail++;
   if (newtail >= MAX_EVENTS)
      newtail = 0;

   event_queue.tail = newtail;

   AVR_LEAVE_CRITICAL_REGION();
   return(object);

}

/**
   @brief This is the main loop task for the MAC.  Called by the main
   "forever" loop or by an OS task or thread.  This function processes
   interrupt events that are stored in the event queue by the radio or
   timer.
 */
void macTask(void)
{
   event_object_t *event;

   event = 0;
   while(mac_event_pending())
   {
      event = mac_get_event();
      switch (event->event)
      {
      case MAC_EVENT_SCAN:
         //UART_PRINT("Beacon Frame\r\n");
         // A beacon has been received as a result of an active scan.
         // Record the pan descriptor and store it until the scan has
         // completed.
         if (!macConfig.associated)
         {
            mac_logPanDescriptors(event->data);
            bmm_buffer_free(event->data); // free buffer
         }
         break;
#if __AVR__
      case MAC_EVENT_TIMER:
         // Timer has expired, just callback the stored function pointer
         ((void (*)(void))event->data)();  // cast data to function pointer
         break;
#endif
      case MAC_EVENT_RX:
         if (macConfig.associated)
         {
            //UART_PRINT("MAC EVENT RX\r\n");

            // Wake up
            if (NODETYPE != ENDDEVICE)
            {
               uint8_t* pData = ((rx_frame_t*)event->data)->data;
               macChildIsAwake((ftData*)(pData));
               // do not free buffer, this is done in later execution
            }

            // Received a data frame, process it or route it
            if (((ftData*)(((rx_frame_t*)event->data)->data))->finalDestAddr == macConfig.shortAddress)
            {
               // This data frame is for this node
               macDataIndication(event->data);
               bmm_buffer_free(event->data);
            }
            else
            {
               // Route the packet up/down stream
               if (NODETYPE != ENDDEVICE)
               {
                  macRouteData(event->data);
                  // do not free buffer, this is done after radioSendData
                  // because the same buffer is used to send the routing packet
               }
               // NODETYPE == ENDNODE
               else
               {
                  bmm_buffer_free(event->data);
               }
            }

         }
         break;
      case MAC_EVENT_BEACON_REQ:
         //UART_PRINT("Beacon REQ\r\n");
         // Generate and send the beacon.
         if ((NODETYPE == ROUTER && macConfig.associated) || (NODETYPE == COORD))
         {
            //UART_PRINT("send Beacon Frame\r\n");
            sendBeaconFrame();
            bmm_buffer_free(event->data); // free buffer
         }
         // NODETYPE == ENDNODE
         else
         {
            bmm_buffer_free(event->data);
         }
         break;
      case MAC_EVENT_ASSOCIATION_REQUEST:
         //UART_PRINT("Association REQ\r\n");
         // Pan Coord has received this request.
         if (NODETYPE == COORD)
         {
            macAssociationResponse(event->data);
            bmm_buffer_free(event->data);
         }
         else if (NODETYPE == ROUTER && macConfig.associated)
         {
            // Send this packet along to parent
            macRouteAssociateRequest(event->data);
            // Do not free buffer! The receiving buffer is used as
            // transmit buffer, the buffer is freed after radioSendData
         }
         // NODETYPE == ENDNODE
         else
         {
            bmm_buffer_free(event->data);
         }
         break;
      case MAC_EVENT_ASSOCIATION_RESPONSE:
         //UART_PRINT("Association Response\r\n");
         // End device has recieved this response.
         if (NODETYPE != COORD)
         {
            uint8_t* data_frame = (uint8_t*)(((rx_frame_t*)event->data)->data);
            if (*((u16*)(data_frame)) == FCF_ASSOC_RESP_DIRECT) // Direct to MAC Addr?
            {
               // this is a new node, do the association thing
               macConfig.associated = true;
               macConfig.parentShortAddress = ((ftAssocRespDirect*)(data_frame))->srcAddr;
               macConfig.shortAddress = ((ftAssocRespDirect*)(data_frame))->shortAddr;
               radioSetShortAddress(macConfig.shortAddress);
               macAssociationConfirm();
               bmm_buffer_free(event->data); // free buffer
            }
            else
            {
               // we are indirect, so just send this packet along.
               if (NODETYPE == ROUTER && macConfig.associated)
               {
                  macRouteAssociateResponse(event->data);
                  // Do not free buffer! The receiving buffer is used as
                  // transmit buffer, the buffer is freed after radioSendData
               }
            }
         }
         // NODETYPE == COORD
         else
         {
            bmm_buffer_free(event->data);
         }
         break;
      case MAC_EVENT_ROUTE:
         // Routing packet received, forward it
         if (NODETYPE == ROUTER && macConfig.associated)
         {
            macForwardRoutingPacket(event->data);
            // Do not free buffer! The receiving buffer is used as
            // transmit buffer, the buffer is freed after radioSendData
         }
         // NODETYPE == ENDNODE || NODETYPE == COORD
         else
         {
            bmm_buffer_free(event->data);
         }
         break;
      case MAC_EVENT_ACCESS:
         // Channel access failure
         appPacketSendAccessFail();
         //there is no buffer to free, event->data is empty
         break;
      case MAC_EVENT_NACK:
         //UART_PRINT("MAC Event NACK \r\n");
         // Packet failed to be sent, alert application
//         appPacketSendFailed();
         //there is no buffer to free, event->data is empty
         break;
      case MAC_EVENT_ACK:
         //UART_PRINT("MAC Event ACK\r\n");
         // Packet got through, alert application
         appPacketSendSucceed();
         //there is no buffer to free, event->data is empty
         break;
      case MAC_EVENT_SEND:
         //UART_PRINT("MAC Event SEND\r\n");
         radioSendData(((rx_frame_t*)(event->data))->length, ((rx_frame_t*)(event->data))->data);
         if(event->callback)
         {
            (*event->callback)(); // execute callback function
         }
         bmm_buffer_free(event->data);
         break;
      default:
         break;
      }
   }
}

/** @} */
/** @} */

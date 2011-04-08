/* Copyright (c) 2010  Dresden Elektronik Ingenieurtechnik GmbH
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
/**
 * @file commands.h
 *
 * @brief File contains all available commands for 'node <-> node' and 'node <-> serial interface' communication.
 *
 * $Id: commands.h,v 1.1.2.1 2010/09/16 08:21:10 ele Exp $
 *
 * @author     Dresden Elektronik: http://www.dresden-elektronik.de
 * @author     Support email: wireless@dresden-elektronik.de
 * @date       2009-12-01
 */

#ifndef COMMANDS_H_
#define COMMANDS_H_

/**
 * @name Commands
 * @{
 */
/** @brief get all connected nodes from coordinator */
#define COMMAND_CHILD_TABLE_REQUEST             (0x01)
/** @brief response from coordinator with a list of connected nodes */
#define COMMAND_CHILD_TABLE_RESPONSE            (0x02)

/** @brief new data frame which is send inside the PAN */
#define COMMAND_DATA_FRAME_REQUEST              (0x03)
/** @brief new data frame which is send outside the PAN */
#define COMMAND_DATA_FRAME_RESPONSE             (0x04)

/** @brief new ping frame which request a ping response */
#define COMMAND_PING_REQUEST                    (0x05)
/** @brief ping response frame (require ping request) */
#define COMMAND_PING_RESPONSE                   (0x06)

/** @brief get information about the node configuration (node type etc.) - only over serial interface */
#define COMMAND_NODE_INFO_REQUEST               (0x07)
/** @brief node info response - only over serial interface */
#define COMMAND_NODE_INFO_RESPONSE              (0x08)

/** @brief request to coordinator to get all actual link quality values from all connected nodes */
#define COMMAND_LINK_QUALITY_REQUEST            (0x10)
/** @brief response to link quality request */
#define COMMAND_LINK_QUALITY_RESPONSE           (0x11)

/** @brief internal link quality request from coordinator to router nodes @see link_quality.c */
#define COMMAND_INTERN_LINK_QUALITY_REQUEST     (0x20)
/** @brief internal link quality responses from router nodes to coordinator @see link_quality.c */
#define COMMAND_INTERN_LINK_QUALITY_RESPONSE    (0x21)

/** @brief not used directly (a status message is generated periodically autonomous) @see status.c */
#define COMMAND_STATUS_REQUEST                  (0x30)
/** @brief status message generated by each node (except coordinator) which is send to coordinator @see status.c */
#define COMMAND_STATUS_RESPONSE                 (0x31)

/** @brief a user data frame from outside PAN send to an arbitrary node. Destination node chnage command to COMMAND_USER_DATA_RESPONSE and send user data frame out over serial interface */
#define COMMAND_USER_DATA_REQUEST               (0x70)
/** @brief user data frame whcih is send out over serial interface (outside PAN) */
#define COMMAND_USER_DATA_RESPONSE              (0x71)

/** @} */

#define NO_OPTION                               (0x00)

#endif /* COMMANDS_H_ */

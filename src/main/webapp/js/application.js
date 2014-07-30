$(function() {
  "use strict";
  // User interface elements
  var connectionIndicator = $('#connection-indicator');
  // Atmosphere socket-related variables
  var socket = $.atmosphere;
  var subSocket;
  var transport = 'websocket';
  /* Set up all the Atmosphere request callbacks. */
  var request = {
    url: "/the-chat",
    contentType: "application/json",
    transport: transport,
    trackMessageLength : true,
    fallbackTransport: 'long-polling'
  };
  // When the Atmosphere connection becomes available,
  // show the connection indicator,
  // and set the transport which the server will support.
  request.onOpen = function(response) {
    connectionIndicator.showConnected()
    transport = response.transport;
  };
  // Subscribe to the Atmosphere socket.
  // This is the crucial piece of the puzzle which actually
  // makes the connection between each browser and the live
  // pipe to the server.
  subSocket = socket.subscribe(request);
  /* User interface code is all below this point */
  // Show the Atmosphere connected icon, a thumbs-up.
  connectionIndicator.showConnected = function(){
    this.addClass('icon-thumbs-up').removeClass('icon-thumbs-down')
  }
  // Show the Atmosphere disconnected icon, a thumbs-down.
  connectionIndicator.showDisconnected = function(){
    this.addClass('icon-thumbs-down').removeClass('icon-thumbs-up')
  }
});
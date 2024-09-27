var stompClient = null;

var socket = new SockJS('/gs-guide-websocket');
stompClient = Stomp.over(socket);




function connect() {
    if(stompClient.connected === true) {
        console.log("connected! ");
    } else  {
        stompClient.connect({}, function(frame) {
            stompClient.subscribe('/order/new', function(messageOutput) {
                console.log("have new order");
                // update new template
                const order = document.querySelector("#order_pag");
                if(order != null) {
                    filterSearch();
                }
            });
            stompClient.subscribe('/order/cancel', function(messageOutput) {
                const order = document.querySelector("#order_pag");
                if(order != null) {
                    const item = document.querySelector("#" + CSS.escape("order" + messageOutput.body));
                    if(item != null) {
                        alert("khách da hủy don hang");
                    }else  {
                        filterSearch();
                    }
                }
                // update new template
                // console.log(JSON.parse(messageOutput.body));
            });
        });
    }
}
connect();





function disconnect() {
    if(stompClient != null) {
        stompClient.disconnect();
    }
    console.log("Disconnected");
}

// function sendMessage() {
//     stompClient.send("/app/chat", {},
//         JSON.stringify({'from':from, 'text':text}));
// }


const paymentStart = (booking_id,amount) => {
    // we will use ajax to send request to server to create order -jquery\
     $("#loader").show();
    $.ajax({
        url: "/utilityservices/payment/create_order",
        data: JSON.stringify({ amount: amount, info: 'order_request',booking_id:booking_id}),
            contentType: 'application/json',
            type: 'POST',
            dataType: 'json',
        success: function (response) {
            //success
            console.log(response.status);

            if (response.status == "created") {
                //open payment form
                let options = {
                    key: "rzp_test_ADEnLyqI9oALQY",
                    amount: response.amount,
                    currency: "INR",
                    name: "Uitility Service Provider",
                    description:
                        "our donation will directly impact the lives of impoverished children, providing them with vital support and opportunities they deserve. With your generosity, we can:",
                    image:
                        "https://www.savethechildren.in/wp-content/uploads/2022/08/Artboard-2-1.webp",
                    order_id: response.id,
                    handler: function (response) {

                        updatePaymentOnServer(response.razorpay_payment_id, response.razorpay_order_id, "paid",booking_id);


                    },
                    prefill: {
                        name: "",
                        email: "",
                        contact: ""
                    },
                    notes: {
                        address: "AT POST Utility Service Provider"
                    },
                    theme: {
                        color: "#3399cc"
                    }

                };
                let razorpay = new Razorpay(options);
                razorpay.on('payment.failed', function (response) {
                    console.log(response.error.code);
                    console.log(response.error.description);
                    console.log(response.error.source);
                    console.log(response.error.step);
                    console.log(response.error.reason);
                    console.log(response.error.metadata.order_id);
                    console.log(response.error.metadata.payment_id);
                    // window.alert("oops!! payment failed");
                    Swal.fire("Failed!!", "Opps!!!payment failed", "error");
                });
                razorpay.open();
                 $("#loader").hide();

            }



        },
        error: function (erro) {

        Swal.fire("Failed!!", "Opps!!! Something Went Wrong", "error");
            //error
            console.log(erro)
             $("#loader").hide();

        }

    })


};


function updatePaymentOnServer(paymentId, orderId, isSuccess,booking_id) {
         $("#loader").show();
    $.ajax({
        url: '/utilityservices/payment/update_order',
        data: JSON.stringify({ payment_id: paymentId, order_id: orderId, status: isSuccess,booking_id: booking_id }),
        contentType: 'application/json',
        type: 'POST',
        dataType: 'json',
        success: function (response) {
            Swal.fire("Congratulation", "payment Succefull", "success");
             $("#loader").hide();
            window.location.href = "/utilityservices/user/index"
        },
        error: function (error) {
            Swal.fire("Failed", "payment Successfull,but we did not get on server,we will contact you as soon as possible", "warning");
             $("#loader").hide();
        }
    })
}


//cash payment


const cashPayment=(booking_id)=>{
                   Swal.fire({
                                      icon: 'payment',
                                      text: 'Service has been Completed',
                                      showCancelButton: true,
                                      confirmButtonColor: '#3085d6',
                                      cancelButtonColor: '#d33',
                                      confirmButtonText: 'Take Payment',
                                      cancelButtonText: 'Cancel'
                                     }).then((result) => {
                                      if (result.isConfirmed) {
                                         window.location.href = "/utilityservices/payment/cash-payment-update/"+booking_id;
                                      }

                                  });

}


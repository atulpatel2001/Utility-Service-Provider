
//model data for pending-->completed

function setData(bookingId){
            fetch("http://localhost:8080/utilityservices/user/set-booking-status/"+bookingId)
           .then(response => response.json())
           .then(data => {

           if(data.schedule == null){
                        $("#booking-id").html(data.bookingDetail.service_request_number);
                        $("#booking_address").html(data.bookingDetail.booking_address);
                        $("#booking-date").html(data.bookingDetail.booking_date);
                        $("#order-track-text-sub1").html(data.bookingDetail.booking_date);
                         $("#order-track-text-sub2").html("Wait For Schedule...");
           }
           else {

             $("#booking-id").html(data.bookingDetail.service_request_number);
             $("#booking_address").html(data.bookingDetail.booking_address);
             $("#booking-date").html(data.bookingDetail.booking_date);
             $("#order-track-text-sub1").html(data.bookingDetail.booking_date);
             if(data.bookingDetail.service_status==="Completed"){
               $("#order-track-text-stat2").html("Shipped "+data.bookingDetail.booking_state+" "+data.bookingDetail.booking_city+" "+data.bookingDetail.booking_pincode);
               $("#order-track-text-sub2").html(data.schedule.schedule_date);
               $("#order-track-text-stat3").html("Estimated:-"+data.bookingDetail.booking_address);
                $("#order-track-text-sub3").html(data.schedule.schedule_date);
             }
             else{
              $("#order-track-text-stat2").html("Shipped "+data.bookingDetail.booking_state+" "+data.bookingDetail.booking_city+" "+data.bookingDetail.booking_pincode);
              $("#order-track-text-sub2").html(data.schedule.schedule_date+".  Service is Pending...");
             }
           }


           })
           .catch(error => {
               console.error('Error fetching item data:', error);
           });

}


/*show service page*/


(function ($) {
    "use strict";

    // Spinner
    var spinner = function () {
        setTimeout(function () {
            if ($('#spinner').length > 0) {
                $('#spinner').removeClass('show');
            }
        }, 1);
    };
    spinner();


    // Initiate the wowjs
    new WOW().init();



    // Dropdown on mouse hover
    const $dropdown = $(".dropdown");
    const $dropdownToggle = $(".dropdown-toggle");
    const $dropdownMenu = $(".dropdown-menu");
    const showClass = "show";

    $(window).on("load resize", function() {
        if (this.matchMedia("(min-width: 992px)").matches) {
            $dropdown.hover(
            function() {
                const $this = $(this);
                $this.addClass(showClass);
                $this.find($dropdownToggle).attr("aria-expanded", "true");
                $this.find($dropdownMenu).addClass(showClass);
            },
            function() {
                const $this = $(this);
                $this.removeClass(showClass);
                $this.find($dropdownToggle).attr("aria-expanded", "false");
                $this.find($dropdownMenu).removeClass(showClass);
            }
            );
        } else {
            $dropdown.off("mouseenter mouseleave");
        }
    });


    // Back to top button
    $(window).scroll(function () {
        if ($(this).scrollTop() > 300) {
            $('.back-to-top').fadeIn('slow');
        } else {
            $('.back-to-top').fadeOut('slow');
        }
    });
    $('.back-to-top').click(function () {
        $('html, body').animate({scrollTop: 0}, 1500, 'easeInOutExpo');
        return false;
    });


    // Facts counter
    $('[data-toggle="counter-up"]').counterUp({
        delay: 10,
        time: 2000
    });

 // Facts counter
    $('[data-toggle="counter-up"]').counterUp({
        delay: 10,
        time: 2000
    });

    // Date and time picker
    $('.date').datetimepicker({
        format: 'L'
    });
    $('.time').datetimepicker({
        format: 'LT'
    });





    // Header carousel
    $(".header-carousel").owlCarousel({
        autoplay: true,
        smartSpeed: 1500,
        items: 1,
        dots: false,
        loop: true,
        nav : true,
        navText : [
            '<i class="bi bi-chevron-left"></i>',
            '<i class="bi bi-chevron-right"></i>'
        ]
    });





    // Testimonials carousel
    $(".testimonial-carousel").owlCarousel({
        autoplay: true,
        smartSpeed: 1000,
        center: true,
        dots: false,
        loop: true,
        nav : true,
        navText : [
            '<i class="bi bi-arrow-left"></i>',
            '<i class="bi bi-arrow-right"></i>'
        ],
        responsive: {
            0:{
                items:1
            },
            768:{
                items:2
            },
            992:{
                items:3
            }
        }
    });
})(jQuery);




//booking data fetch  and validate
//$(document).ready(function() {
//    $("#bookingForm").validate({
//        rules: {
//            booking_person: {
//                required: true,
//                minlength: 7,
//
//            },
//            booking_email: {
//                required: true,
//                email: true
//            },
//            booking_phone: {
//                required: true,
//                pattern: "\\d{10,11}"
//            },
//            booking_city: {
//                required: true
//            },
//            booking_state: {
//                required: true
//            },
//            booking_pincode: {
//                required: true,
//                pattern: "\\d{6}"
//            },
//            booking_address: {
//                required: true
//            }
//        },
//        messages: {
//            booking_person: {
//                required: "Name must be required",
//                minlength: "Name must be between 7 and 20 characters",
//
//            },
//            booking_email: {
//                required: "Email must be Required",
//                email: "Email is Not Valid"
//            },
//            booking_phone: {
//                required: "Phone Number must be Required",
//                pattern: "Phone Number is Not Valid"
//            },
//            booking_city: {
//                required: "City must be Required"
//            },
//            booking_state: {
//                required: "State must be Required"
//            },
//            booking_pincode: {
//                required: "Pincode must be Required",
//                pattern: "Pincode is Not Valid"
//            },
//            booking_address: {
//                required: "Address must be Required"
//            }
//        },
//        submitHandler: function(form) {
//            let formData =new FormData(form);
//             $.ajax({
//                            url: "/utilityservices/user/handle-applydata",
//                            type: 'POST',
//                            data: formData,
//                            success: function(data, textStatus, jqXHR) {
//                             //showResponse(data)
//                             console.log("hello");
//                            },
//                            error: function(jqXHR, textStatus, errorThrown) {
//
//                                alert("An error occurred while processing the form.");
//                            } ,
//                            processData: false,
//                            contentType: false
//                        });
//        }
//    });
//});
$(document).ready(function() {

    $("#bookingForm").submit(function(event) {
      $("#loader").show();
        event.preventDefault(); // Prevent the default form submission

        let formData = new FormData(this);

        $.ajax({
            url: "/utilityservices/user/handle-applydata",
            type: 'POST',
            data: formData,
            success: function(data, textStatus, jqXHR) {
                console.log(data);
                if(data.success==1){
                  $("#loader").hide();
                    showResponse(data);
                }
                else{
                 $("#loader").hide();
                    Swal.fire("Do not Service in This Location so please give suggestion for your pincode service ","","error");
                }
            },
            error: function(jqXHR, textStatus, errorThrown) {
                console.error("An error occurred while processing the form.");
                // Handle the error here
            },
            processData: false,
            contentType: false
        });
    });
});


function showResponse(response) {

  Swal.fire({                         icon: 'question',
                                      showCancelButton: true,
                                      confirmButtonColor: '#3085d6',
                                      cancelButtonColor: '#d33',
                                      confirmButtonText: 'Online Payment',
                                      cancelButtonText: 'Cash Payment'
                                  }).then((result) => {
                                      if (result.isConfirmed) {
                                          paymentStart(response.booking_id,response.amount);
                                      }
                                      else {
                                           window.location.href = "/utilityservices/payment/cash-payment/"+response.booking_id;
                                      }
                                  });
}
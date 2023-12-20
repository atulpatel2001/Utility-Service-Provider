//$(document).ready(function() {
//    $('#form').on('submit', function(event) {
//      event.preventDefault();
//       let form = new FormData(this);
//        let password =$('#password').val();
//        let confirmPassword = $('#password2').val();
//        console.log(password);
//        console.log(confirmPassword);
//        if (password.trim() !== confirmPassword.trim()) {
//swal("Password dose not Match!", {
//  button: false,
//});
//return;
//        }
//
//        else if(password.trim() === confirmPassword.trim()){
//                       $.ajax({
//                                    url: "/utilityservices/home/doSignup",
//                                    type: 'POST',
//                                    data: form,
//                                    success: function (data, textStatus, jqXHR) {
//                                        console.log(data)
//                                            if(data.trim() === 'success'){
//                                                  swal("Good job!", "Registered Successfully. We are going Redirect to Login Page!", "success")
//                                                .then((value) => {
//                                                   window.location="/utilityservices/home/signin";
//                                                });
//                                            }
//                                            else{
//                                               swal("Error!", "!Please Try Again", "error");
//                                            }
//                                    },
//                                    error: function (jqXHR, textStatus, errorThrown) {
//                                        console.log(jqXHR)
//                                        swal("Something Went Wrong !!! TryAgain!!");
//                                    },
//                                    processData: false,
//                                    contentType: false
//                                });
//        }
//    });
//});
//
//
    $(document).ready(function() {
        $('#form').validate({
            rules: {
                user_name: {
                    required: true
                },
                user_email: {
                    required: true,
                    email: true
                },
                user_password: {
                    required: true,
                    minlength: 6
                },
                password2: {
                    required: true,
                    equalTo: "#password"
                },
                user_termAndCondition: {
                    required: true
                }
            },
            messages: {
                password2: {
                    equalTo: "Passwords do not match!"
                },
                user_termAndCondition: {
                    required: "Please accept the Terms and Conditions."
                }
            },
            submitHandler: function(form) {
                let password = $('#password').val();
                let confirmPassword = $('#password2').val();
                if (password.trim() !== confirmPassword.trim()) {
                   Swal.fire(
                     'Password does not match?',
                     'Try Again',
                     'question'
                   )
                } else {
                    let formData = new FormData(form);
                    $.ajax({
                        url: "/utilityservices/home/doSignup",
                        type: 'POST',
                        data: formData,
                        success: function(data, textStatus, jqXHR) {
                            if (data.trim() === 'success') {
                                Swal.fire("Good job!", "Registered Successfully. We are going Redirect to Login Page!", "success")
                                    .then((value) => {
                                        window.location = "/utilityservices/home/signin";
                                    });
                            } else {
                                Swal.fire("Please Try Again ",data);
                            }
                        },
                        error: function(jqXHR, textStatus, errorThrown) {
                            console.log(jqXHR);
                            Swal.fire("Something Went Wrong !!! Try Again!!");
                        },
                        processData: false,
                        contentType: false
                    });
                }
            }
        });
    });


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


    // Testimonials carousel
    $(".testimonial-carousel").owlCarousel({
        autoplay: true,
        smartSpeed: 1000,
        items: 1,
        dots: false,
        loop: true,
        nav: true,
        navText : [
            '<i class="bi bi-chevron-left"></i>',
            '<i class="bi bi-chevron-right"></i>'
        ]
    });


})(jQuery);

//forgot password
function forgotPassword(){
    $('.email').show();

}

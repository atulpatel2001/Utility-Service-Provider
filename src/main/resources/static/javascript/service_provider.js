 const navItems = document.querySelectorAll('.side-nav__item');
const removeClasses = () => {
  navItems.forEach(eachItem => {
    eachItem.classList.remove('side-nav__item-active');
  });
}

navItems.forEach(eachItem => {
  eachItem.addEventListener('click', function() {
      removeClasses();
    eachItem.classList.add('side-nav__item-active');
  });
});


 $(document).ready(function() {
  $("#changePassword").submit(function(event) {
          event.preventDefault(); // Prevent the default form submission
          let password = $('#password').val();
          let confirmPassword = $('#confirmPassword').val();
          if (password.trim() !== confirmPassword.trim()) {
                              Swal.fire(
                              'Password does not match?',
                              'Try Again',
                              'question'
                            )
                         }
                         else{
         $.ajax({
             url: "/utilityservices/service-provider/change-password",
             type: 'POST',
             data: password,
             success: function(data, textStatus, jqXHR) {
                Swal.fire({
                  position: "top-end",
                  icon: "success",
                  title: "SuccessFully Update Password",
                  showConfirmButton: false,
                  timer: 1500
                });
             },
             error: function(jqXHR, textStatus, errorThrown) {
                Swal.fire({
                                  position: "top-end",
                                  icon: "warning",
                                  title: "Something Went Wrong!!!",
                                  showConfirmButton: false,
                                  timer: 1500
                                });
             },
             processData: false,
             contentType: false
         });

         }
     });
 });


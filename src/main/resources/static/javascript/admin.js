/*dashboard*/
 const body = document.querySelector("body"),
      modeToggle = body.querySelector(".mode-toggle");
      sidebar = body.querySelector("nav");
      sidebarToggle = body.querySelector(".sidebar-toggle");

let getMode = localStorage.getItem("mode");
if(getMode && getMode ==="dark"){
    body.classList.toggle("dark");
}

let getStatus = localStorage.getItem("status");
if(getStatus && getStatus ==="close"){
    sidebar.classList.toggle("close");
}

modeToggle.addEventListener("click", () =>{
    body.classList.toggle("dark");
    if(body.classList.contains("dark")){
        localStorage.setItem("mode", "dark");
    }else{
        localStorage.setItem("mode", "light");
    }
});

sidebarToggle.addEventListener("click", () => {
    sidebar.classList.toggle("close");
    if(sidebar.classList.contains("close")){
        localStorage.setItem("status", "close");
    }else{
        localStorage.setItem("status", "open");
    }
})


/*service man request*/
function getRequest(){
  $.ajax({
    url: "/utilityservices/admin/send-request", // Replace with your server endpoint URL
    method: "GET",
    dataType: "json", // Change the data type based on the response from the server
    success: function(data) {

//        $("#request").text("<table class='table table-dark'><tbody> <tr><th scope='row'>"+data.serviceMan.user.+"</th><td>Mark</td> <td>Otto</td> <td>@mdo</td></tr></tbody></table>")
        //createTable(data);


//        $("#request").html("<h1>hello</h1>");
createTable(data);
         $("#request").show(); // If data exists, show the div tag

    },
    error: function(xhr, status, error) {
      console.error("Error fetching data:", error);
    }
  });

}



function createTable(data) {
  var tableHTML = "<table class='table'><thead><tr><th>Name</th><th>Request Date</th><th>Action</th></tr></thead><tbody>";

  // Loop through the data and create rows for the table
  data.forEach(function(serviceMan) {
    tableHTML += '<tr><td>' + serviceMan.user.user_name + '</td><td>' + serviceMan.user.user_joinDate + '</td><td><a class="btn btn-primary btn-sm" href="/utilityservices/admin/'+serviceMan.serviceman_id+'/serviceman-detail"> Read More</a> | <a class="btn btn-primary btn-sm" href="/utilityservices/admin/' + serviceMan.serviceman_id + '/accept-request">Accept</a>| <a class="btn btn-primary btn-sm" href="/utilityservices/admin/' + serviceMan.serviceman_id + '/decline-request">Decline</a></td></tr>';
  });

  tableHTML += '</tbody></table>';

  // Append the table to the dataContainer div
  $("#request").html(tableHTML);
}



//user change role
  $(document).ready(function () {
         $('.category-select').change(function () {
             var selectedRole = $(this).val();
             var userId = $(this).data('user-id');
             // Send an AJAX request to update the user's role
             $.ajax({
                 type: 'POST',
                 url: '/utilityservices/admin/updateUserRole', // Replace with the actual URL
                 data: {
                     userId: userId,
                     newRole: selectedRole
                 },
                 success: function (response) {
                     // Handle success, e.g., display a success message
                    Swal.fire("Role is Updated","", "success");
                 },
                 error: function (xhr, status, error) {
                     // Handle error, e.g., display an error message
                   Swal.fire("Role is not Updated","","error");
                 }
             });
         });
     });


//categoey manage

//show add category form
$(document).ready(function(){

        $('.add-category-button').click(function(){
                    $('.show-category').hide();
                    $('#add-category').show();

        })
})

//show all categories
$(document).ready(function(){

        $('.show-category-button').click(function(){
                    $('#add-category').hide();
                     $('.show-category').show();

        })
})

//   delete category
    $(document).ready(function () {
                          $('.delete-category').click(function () {
                              var category_id = $(this).data('category_id');
                              const swalWithBootstrapButtons = Swal.mixin({
                                  customClass: {
                                      confirmButton: 'btn btn-success ml-2',
                                      cancelButton: 'btn btn-danger'
                                  },
                                  buttonsStyling: false,
                              });

                              swalWithBootstrapButtons.fire({
                                  title: 'Are you sure?',
                                  text: 'You are about to delete this Category.',
                                  icon: 'warning',
                                  showCancelButton: true,
                                  confirmButtonText: 'Yes, delete it!',
                                  cancelButtonText: 'No, cancel!',
                                  reverseButtons: true,
                              }).then((result) => {
                                  if (result.isConfirmed) {
                                      // User confirmed, send an AJAX request to delete the user
                                      $.ajax({
                                          type: 'POST',
                                          url:'/utilityservices/admin/delete-category', // Replace with the actual URL
                                          data: {
                                              category_id: category_id
                                          },
                                          success: function (response) {
                                              // Handle success, e.g., display a success message
                                              swalWithBootstrapButtons.fire(
                                                  'Deleted!',
                                                  'The Category has been deleted.',
                                                  'success'
                                              );

                                               $(this).closest('tr').remove();
                                          },
                                          error: function (xhr, status, error) {
                                              // Handle error, e.g., display an error message
                                              swalWithBootstrapButtons.fire(
                                                  'Error!',
                                                  'An error occurred while deleting the Category.',
                                                  'error'
                                              );
                                          }
                                      });
                                  } else if (result.dismiss === Swal.DismissReason.cancel) {
                                      swalWithBootstrapButtons.fire(
                                          'Cancelled',
                                          'The Category has not been deleted.',
                                          'error'
                                      );
                                  }
                              });
                          });
                      });



//   delete Service
    $(document).ready(function () {
                          $('.delete-service').click(function () {
                              var service_id = $(this).data('service_id');
                              const swalWithBootstrapButtons = Swal.mixin({
                                  customClass: {
                                      confirmButton: 'btn btn-success ml-2',
                                      cancelButton: 'btn btn-danger'
                                  },
                                  buttonsStyling: false,
                              });

                              swalWithBootstrapButtons.fire({
                                  title: 'Are you sure?',
                                  text: 'You are about to delete this Category.',
                                  icon: 'warning',
                                  showCancelButton: true,
                                  confirmButtonText: 'Yes, delete it!',
                                  cancelButtonText: 'No, cancel!',
                                  reverseButtons: true,
                              }).then((result) => {
                                  if (result.isConfirmed) {
                                      // User confirmed, send an AJAX request to delete the user
                                      $.ajax({
                                          type: 'POST',
                                          url:'/utilityservices/admin/delete-service', // Replace with the actual URL
                                          data: {
                                              service_id: service_id
                                          },
                                          success: function (response) {
                                              // Handle success, e.g., display a success message
                                              swalWithBootstrapButtons.fire(
                                                  'Deleted!',
                                                  'The Service has been deleted.',
                                                  'success'
                                              );

                                               $(this).closest('tr').remove();
                                          },
                                          error: function (xhr, status, error) {
                                              // Handle error, e.g., display an error message
                                              swalWithBootstrapButtons.fire(
                                                  'Error!',
                                                  'An error occurred while deleting the Service.',
                                                  'error'
                                              );
                                          }
                                      });
                                  } else if (result.dismiss === Swal.DismissReason.cancel) {
                                      swalWithBootstrapButtons.fire(
                                          'Cancelled',
                                          'The Service has not been deleted.',
                                          'error'
                                      );
                                  }
                              });
                          });
                      });



//show add service form
$(document).ready(function(){

        $('.add-service-button').click(function(){
                    $('.show-services').hide();
                    $('#add-service').show();

        })
})

//show all service
$(document).ready(function(){

        $('.show-service-button').click(function(){
                    $('#add-service').hide();
                     $('.show-services').show();

        })
})


     //--------------delete user
   $(document).ready(function () {
               $('.delete').click(function () {
                   var userId = $(this).data('user-id');
                   const swalWithBootstrapButtons = Swal.mixin({
                       customClass: {
                           confirmButton: 'btn btn-success ml-2',
                           cancelButton: 'btn btn-danger'
                       },
                       buttonsStyling: false,
                   });

                   swalWithBootstrapButtons.fire({
                       title: 'Are you sure?',
                       text: 'You are about to delete this user.',
                       icon: 'warning',
                       showCancelButton: true,
                       confirmButtonText: 'Yes, delete it!',
                       cancelButtonText: 'No, cancel!',
                       reverseButtons: true,
                   }).then((result) => {
                       if (result.isConfirmed) {
                           // User confirmed, send an AJAX request to delete the user
                           $.ajax({
                               type: 'POST',
                               url:'/utilityservices/admin/deleteUser', // Replace with the actual URL
                               data: {
                                   userId: userId
                               },
                               success: function (response) {
                                   // Handle success, e.g., display a success message
                                   swalWithBootstrapButtons.fire(
                                       'Deleted!',
                                       'The user has been deleted.',
                                       'success'
                                   );

                                    $(this).closest('tr').remove();
                               },
                               error: function (xhr, status, error) {
                                   // Handle error, e.g., display an error message
                                   swalWithBootstrapButtons.fire(
                                       'Error!',
                                       'An error occurred while deleting the user.',
                                       'error'
                                   );
                               }
                           });
                       } else if (result.dismiss === Swal.DismissReason.cancel) {
                           swalWithBootstrapButtons.fire(
                               'Cancelled',
                               'The user has not been deleted.',
                               'error'
                           );
                       }
                   });
               });
           });

        //---------------------delete ServiceMan
           $(document).ready(function () {
                          $('.delete-serviceMan').click(function () {
                              var serviceman_id = $(this).data('serviceman_id');
                              const swalWithBootstrapButtons = Swal.mixin({
                                  customClass: {
                                      confirmButton: 'btn btn-success ml-2',
                                      cancelButton: 'btn btn-danger'
                                  },
                                  buttonsStyling: false,
                              });

                              swalWithBootstrapButtons.fire({
                                  title: 'Are you sure?',
                                  text: 'You are about to delete this ServiceMan.',
                                  icon: 'warning',
                                  showCancelButton: true,
                                  confirmButtonText: 'Yes, delete it!',
                                  cancelButtonText: 'No, cancel!',
                                  reverseButtons: true,
                              }).then((result) => {
                                  if (result.isConfirmed) {
                                      // User confirmed, send an AJAX request to delete the user
                                      $.ajax({
                                          type: 'POST',
                                          url:'/utilityservices/admin/deleteServiceMan', // Replace with the actual URL
                                          data: {
                                              serviceman_id: serviceman_id
                                          },
                                          success: function (response) {
                                              // Handle success, e.g., display a success message
                                              swalWithBootstrapButtons.fire(
                                                  'Deleted!',
                                                  'The ServiceMan has been deleted.',
                                                  'success'
                                              );

                                               $(this).closest('tr').remove();
                                          },
                                          error: function (xhr, status, error) {
                                              // Handle error, e.g., display an error message
                                              swalWithBootstrapButtons.fire(
                                                  'Error!',
                                                  'An error occurred while deleting the ServiceMan.',
                                                  'error'
                                              );
                                          }
                                      });
                                  } else if (result.dismiss === Swal.DismissReason.cancel) {
                                      swalWithBootstrapButtons.fire(
                                          'Cancelled',
                                          'The ServiceMan has not been deleted.',
                                          'error'
                                      );
                                  }
                              });
                          });
                      });


         //delete booking


         $(document).ready(function(){
         $('.delete-booking').click(function () {

                     var booking_id = $(this).data('booking-id');
                                                  const swalWithBootstrapButtons = Swal.mixin({
                                                      customClass: {
                                                          confirmButton: 'btn btn-success ml-2',
                                                          cancelButton: 'btn btn-danger'
                                                      },
                                                      buttonsStyling: false,
                                                  });

                                                  swalWithBootstrapButtons.fire({
                                                      title: 'Are you sure?',
                                                      text: 'You are about to delete this Booking.',
                                                      icon: 'warning',
                                                      showCancelButton: true,
                                                      confirmButtonText: 'Yes, delete it!',
                                                      cancelButtonText: 'No, cancel!',
                                                      reverseButtons: true,
                                                  }).then((result) => {
                                                      if (result.isConfirmed) {
                                                          // User confirmed, send an AJAX request to delete the user
                                                          $.ajax({
                                                              type: 'POST',
                                                              url:'/utilityservices/admin/deleteBooking', // Replace with the actual URL
                                                              data: {
                                                                  booking_id: booking_id
                                                              },
                                                              success: function (response) {
                                                                  // Handle success, e.g., display a success message
                                                                  swalWithBootstrapButtons.fire(
                                                                      'Deleted!',
                                                                      'The Booking has been deleted.',
                                                                      'success'
                                                                  );

                                                                   $(this).closest('tr').remove();
                                                              },
                                                              error: function (xhr, status, error) {
                                                                  // Handle error, e.g., display an error message
                                                                  swalWithBootstrapButtons.fire(
                                                                      'Error!',
                                                                      'An error occurred while deleting  Booking.',
                                                                      'error'
                                                                  );
                                                              }
                                                          });
                                                      } else if (result.dismiss === Swal.DismissReason.cancel) {
                                                          swalWithBootstrapButtons.fire(
                                                              'Cancelled',
                                                              'Booking has not been deleted.',
                                                              'error'
                                                          );
                                                      }
                                                  });
                    })
         })
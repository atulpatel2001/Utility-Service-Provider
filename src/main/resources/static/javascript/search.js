                                      //search user
const searchUser=()=>{

let query=$("#search-user").val();

if (query == '') {
        $(".search-result").hide()
    }

    else{
         const url="http://localhost:8080/utilityservices/search/search-user/"+query;

         fetch(url).then(response=>{
               return response.json();
         })
         .then((data) => {
                     let tableHtml = '';

                                               data.forEach(user => {
                                                   let table = userTable(user);
                                                   tableHtml += table;
                                               });

                                               $(".search-result").html(tableHtml);
                                               $(".search-result").show();
                });
    }
}

function userTable(user) {
    let table = '<div class="table-responsive">' +
        '<table class="table no-wrap user-table mb-0">' +
        '<tbody>';
        table += '<tr>' +
            '<td class="pl-4">#USER' + user.user_id + '</td>' +
            '<td><h5 class="font-medium mb-0">' + user.user_name + '</h5></td>' +
            '<td><span class="text-muted">' + user.user_email + '</span><br></td>' +
            '<td><span class="text-muted">' + user.user_joinDate + '</span><br></td>' +
            '<td>'  +
            '<td><span class="text-muted">' + user.user_role + '</span><br></td>' +

            '</select>'
            '</td>' +
            '<td>' +

            '</td>' +
            '</tr>';
            /* '<button type="button" class="btn btn-outline-info btn-circle btn-lg btn-circle"><i class="fa fa-key"></i></button>' +
                          '<button type="button" class="btn btn-outline-info btn-circle btn-lg btn-circle ml-2 delete" id="delete-user" data-user-id="'+user.user_id+'"><i class="fa fa-trash"></i></button>' +
                          '<button type="button" class="btn btn-outline-info btn-circle btn-lg btn-circle ml-2"><i class="fa fa-edit"></i></button>' +
                          '<button type="button" class="btn btn-outline-info btn-circle btn-lg btn-circle ml-2"><i class="fa fa-upload"></i></button>' +
            */

    table += '</tbody></table></div>';
    return table;
}

                                               //search serviceMan

const searchServiceMan=()=>{

let query=$("#search-service-man").val();

if (query == '') {
        $(".search-result-service-man").hide()
    }

    else{
         const url="http://localhost:8080/utilityservices/search/search-service-man/"+query;

         fetch(url).then(response=>{
               return response.json();
         })
         .then((data) => {
                     let tableHtml = '';

                           data.forEach(serviceMan => {
                               let table = serviceManTable(serviceMan);
                               tableHtml += table;
                           });

                           $(".search-result-service-man").html(tableHtml);
                           $(".search-result-service-man").show();

                 });
    }

}

function serviceManTable(serviceMan) {
    let table = '<div class="table-responsive">' +
        '<table class="table no-wrap user-table mb-0">' +
        '<tbody>';

    table += '<tr>' +
        '<td class="pl-4">#SERVICE' + serviceMan.serviceman_id + '</td>' +
        '<td><h5 class="font-medium mb-0">' + serviceMan.user.user_name + '</h5></td>' +
        '<td><span class="text-muted">' + serviceMan.user.user_email + '</span><br></td>' +
        '<td><span class="text-muted">' + serviceMan.user.user_joinDate + '</span><br></td>' +
        '<td><span class="text-muted">' + serviceMan.user.user_role + '</span><br></td>' +
        '<td>';

    table += '<a type="button" class="btn btn-outline-info btn-circle btn-lg btn-circle" ' +
        'href="/utilityservices/admin/' + serviceMan.serviceman_id + '/serviceman-detail"><i class="fa fa-info-circle"></i></a>' +
        '<a type="button" class="btn btn-outline-info btn-circle btn-lg btn-circle ml-2" ' +
        'href="/utilityservices/admin/adhar-card/' + serviceMan.serviceman_id + '"><i class="fa fa-upload"></i></a>';


    table += '</td></tr></tbody></table></div>';
   return table;
}



                                                      //search Pending service
const searchPendingService=()=>{

      let query=$("#search-pending-service").val();
    if (query == '') {
            $(".search-result-pending-service").hide();
        }
        else{
             const url="http://localhost:8080/utilityservices/search/search-pending-service/"+query;
             fetch(url).then(response=>{
                   return response.json();
             })
             .then((data) => {


                       let tableHtml = ''; // Initialize an empty string to accumulate the HTML

                           data.forEach(pendingService => {
                               let table = pendingServiceTable(pendingService);
                               tableHtml += table; // Append the table HTML to the accumulator
                           });

                           // Set the accumulated HTML content after the loop has completed
                           $(".search-result-pending-service").html(tableHtml);
                           $(".search-result-pending-service").show();
        });
}
}

function pendingServiceTable(pendingService) {
    let table = '<div class="table-responsive">' +
        '<table class="table no-wrap user-table mb-0">' +
        '<tbody>';

    table += '<tr>' +
        '<td class="pl-4">#SERVICE' + pendingService.bookingDetail.service_request_number + '</td>' +
        '<td><h5 class="font-medium mb-0">' + pendingService.bookingDetail.booking_person + '</h5></td>' +
        '<td><span class="text-muted">' + pendingService.bookingDetail.booking_email + '</span><br></td>' +
        '<td><span class="text-muted">' + pendingService.bookingDetail.service_status + '</span><br></td>' +
        '<td><span class="text-muted">' + pendingService.bookingDetail.booking_date + '</span><br></td>' +
         '<td><span class="text-muted">' + pendingService.serviceMan.user.user_name + '</span><br></td>' +
        '<td>';

    table += '<a type="button" class="btn btn-outline-info btn-circle btn-lg btn-circle" ' +
        'href="/utilityservices/admin/' + pendingService.allocate_booking_id + '/booking-detail"><i class="fa fa-info-circle"></i></a> ';


    table += '</td></tr></tbody></table></div>';
   return table;
}

                                           //serach Complete Service

const searchCompletedService=()=>{
      let query=$("#search-completed-service").val();
    if (query == '') {
            $(".search-result-completed-service").hide();
        }
        else{
             const url="http://localhost:8080/utilityservices/search/search-completed-service/"+query;
             fetch(url).then(response=>{
                   return response.json();
             })


              .then((data) => {
                                          let tableHtml = '';

                                        data.forEach(completedService => {
                                            let table =  completedServiceTable(completedService);
                                            tableHtml += table;
                                        });
                                        $(".search-result-completed-service").html(tableHtml);
                                        $(".search-result-completed-service").show();
                     });

        }
}

function completedServiceTable(completedService) {
    let table = '<div class="table-responsive">' +
        '<table class="table no-wrap user-table mb-0">' +
        '<tbody>';

    table += '<tr>' +
        '<td class="pl-4">#SERVICE' + completedService.bookingDetail.service_request_number + '</td>' +
        '<td><h5 class="font-medium mb-0">' + completedService.bookingDetail.booking_person + '</h5></td>' +
        '<td><span class="text-muted">' + completedService.bookingDetail.booking_email + '</span><br></td>' +
        '<td><span class="text-muted">' + completedService.bookingDetail.service_status + '</span><br></td>' +
        '<td><span class="text-muted">' + completedService.bookingDetail.booking_date + '</span><br></td>' +
         '<td><span class="text-muted">' + completedService.serviceMan.user.user_name + '</span><br></td>' +
        '<td>';

    table += '<a type="button" class="btn btn-outline-info btn-circle btn-lg btn-circle" ' +
        'href="/utilityservices/admin/' + completedService.allocate_booking_id + '/booking-detail"><i class="fa fa-info-circle"></i></a> ';

    table += '</td></tr></tbody></table></div>';
    return table;

}


// search Category



const searchCategory=()=>{
      let query=$("#search-category").val();
    if (query == '') {
            $(".search-result-category").hide();
        }
        else{
             const url="http://localhost:8080/utilityservices/search/search-category/"+query;
             fetch(url).then(response=>{
                   return response.json();
             })
              .then((data) => {
                                        let tableHtml = '';
                                        data.forEach(category => {
                                            let table =  categoryTable(category);
                                            tableHtml += table;
                                        });
                                        $(".search-result-category").html(tableHtml);
                                        $(".search-result-category").show();
                     });

        }
}

function categoryTable(category) {
    let table = '<div class="table-responsive">' +
        '<table class="table no-wrap user-table mb-0">' +
        '<tbody>';

    table += '<tr>' +
        '<td class="pl-4">#CATEGORY' + category.category_id + '</td>' +
        '<td><h5 class="font-medium mb-0">' + category.category_name + '</h5></td>' +
        '<td><span class="text-muted">' + category.addCategoryDate + '</span><br></td>';

    table += '</tr></tbody></table></div>';
    return table;
}

const searchService=()=>{
      let query=$("#search-service").val();
    if (query == '') {
            $(".search-result-service").hide();
        }
        else{
             const url="http://localhost:8080/utilityservices/search/search-service/"+query;
             fetch(url).then(response=>{
                   return response.json();
             })
              .then((data) => {
                                        let tableHtml = '';
                                        data.forEach(service => {
                                            let table =  serviceTable(service);
                                            tableHtml += table;
                                        });
                                        $(".search-result-service").html(tableHtml);
                                        $(".search-result-service").show();
                     });

        }
}

function serviceTable(service) {
    let table = '<div class="table-responsive">' +
        '<table class="table no-wrap user-table mb-0">' +
        '<tbody>';

    table += '<tr>' +
        '<td class="pl-4">#SERVICE' + service.service_id + '</td>' +
        '<td><h5 class="font-medium mb-0">' + service.service_name + '</h5></td>' +
        '<td><span class="text-muted">' + service.addServiceDate + '</span><br></td>';

    table += '</tr></tbody></table></div>';
    return table;
}



//service man pending service

const searchPendingServiceForServiceMan=()=>{
      let query=$("#search-pending-service-serviceman").val();
    if (query == '') {
            $(".search-result-pending-service-serviceman").hide();
        }
        else{
             const url="http://localhost:8080/utilityservices/search/search-pending-service-for-serviceman/"+query;
             fetch(url).then(response=>{
                   return response.json();
             })
              .then((data) => {
                                         let tableHtml = ''; // Initialize an empty string to accumulate the HTML

                                                                  data.forEach(pendingService => {
                                                                      let table = pendingServiceForServiceManTable(pendingService);
                                                                      tableHtml += table; // Append the table HTML to the accumulator
                                                                  });

                                                                  // Set the accumulated HTML content after the loop has completed
                                                                  $(".search-result-pending-service-serviceman").html(tableHtml);
                                                                  $(".search-result-pending-service-serviceman").show();
                     });

        }
}

function pendingServiceForServiceManTable(pendingService) {
    let table = '<div class="table-responsive">' +
            '<table class="table no-wrap user-table mb-0">' +
            '<tbody>';

        table += '<tr>' +
            '<td class="pl-4">#' + pendingService.bookingDetail.service_request_number + '</td>' +
            '<td><h5 class="font-medium mb-0">' + pendingService.bookingDetail.booking_person + '</h5></td>' +
            '<td><span class="text-muted">' + pendingService.bookingDetail.booking_email + '</span><br></td>' +
            '<td><span class="text-muted">' + pendingService.bookingDetail.service_status + '</span><br></td>' +
            '<td><span class="text-muted">' + pendingService.bookingDetail.booking_date + '</span><br></td>' +
            '<td>';

        table += '<a type="button" class="btn btn-outline-info btn-circle btn-lg btn-circle" ' +
            'href="/utilityservices/service-provider/' + pendingService.bookingDetail.booking_id + '/service-info"><i class="fa fa-info-circle"></i></a> ';


        table += '</td></tr></tbody></table></div>';
       return table;
}


const searchCompletedServiceForServiceMan=()=>{
      let query=$("#search-completed-service-serviceman").val();
    if (query == '') {
            $(".search-result-completed-service-serviceman").hide();
        }
        else{
             const url="http://localhost:8080/utilityservices/search/search-completed-service-for-serviceman/"+query;
             fetch(url).then(response=>{
                   return response.json();
             })
              .then((data) => {
                                         let tableHtml = ''; // Initialize an empty string to accumulate the HTML

                                                                  data.forEach(completedService => {
                                                                      let table = completedServiceForServiceManTable(completedService);
                                                                      tableHtml += table; // Append the table HTML to the accumulator
                                                                  });

                                                                  // Set the accumulated HTML content after the loop has completed
                                                                  $(".search-result-completed-service-serviceman").html(tableHtml);
                                                                  $(".search-result-completed-service-serviceman").show();
                     });

        }
}

function completedServiceForServiceManTable(completedService) {
    let table = '<div class="table-responsive">' +
            '<table class="table no-wrap user-table mb-0">' +
            '<tbody>';

        table += '<tr>' +
            '<td class="pl-4">#' +completedService.bookingDetail.service_request_number + '</td>' +
            '<td><h5 class="font-medium mb-0">' + completedService.bookingDetail.booking_person + '</h5></td>' +
            '<td><span class="text-muted">' + completedService.bookingDetail.booking_email + '</span><br></td>' +
            '<td><span class="text-muted">' + completedService.bookingDetail.service_status + '</span><br></td>' +
            '<td><span class="text-muted">' + completedService.bookingDetail.booking_date + '</span><br></td>' +
            '<td>';

        table += '<a type="button" class="btn btn-outline-info btn-circle btn-lg btn-circle" ' +
            'href="/utilityservices/service-provider/' + completedService.bookingDetail.booking_id + '/service-info"><i class="fa fa-info-circle"></i></a> ';


        table += '</td></tr></tbody></table></div>';
       return table;
}





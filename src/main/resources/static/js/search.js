$(document).ready(function () {

    $("#search-form").submit(function (event) {


        //stop submit the form, we will post it manually.
        event.preventDefault();

        fire_ajax_submit();

    });

});

function fire_ajax_submit() {

    let search = {}
    search["keyword"] = $("#keyword").val();
    search["propertyType"] = $("#propertyType").children("option:selected").val();
    search["location"] = $("#location").val();
    search["price"] = $("#price").children("option:selected").val();

    $("#bth-search").prop("disabled", true);

    let token = $("meta[name='_csrf']").attr("content");

    $.ajax({
        type: "POST",
        contentType: "application/json",
        url: "/search",
        headers: {"X-CSRF-TOKEN": token},
        data: JSON.stringify(search),
        dataType: 'json',
        cache: false,
        timeout: 600000,
        success: function (data) {


            let next = 1;
            let collectAll = "";

            if (data.result == null){

                collectAll +=  '<div class="col-md-12 heading-section text-center">' +
                  '<h4 class="mb-2 text-danger">'+ data.message +'</h4>' +
                    '</div>';
            }

            if(data.result != null){
                $.each(data.result, function (index,value) {

                    let im = '<img class="card-img-top" src="'+ value.photos.url +'" alt="Card image cap">';
                    let link = '<a href="/property-details/'+ value.propertyName +'" class="btn btn-primary">Click here for more details!</a>'

                    collectAll +=  '<div class="col-md-4">' +
                        '<div class="card text-white bg-dark mb-3" style="width: 18rem;">' +
                        im +
                        '<div class="card-body">' +
                        '<h5 class="card-title text-white">'+ value.town.name +
                        '<p class="card-text">'+ value.address.area +'</p>' +'</h5>' +
                        '<p class="card-text">'+ value.description +'</p>' +
                        link +
                        '</div>'+
                        '</div>' +
                        '</div>';
                    next++;
                });
            }



             $('#feedback').html(collectAll);

            console.log("SUCCESS : ", data);
            $("#bth-search").prop("disabled", false);

        },
        error: function (e) {

            let json = "<h4>Ajax Response</h4>"
             + "<p>"  + e.responseText + "</p>";
            $('#feedback').html(json);

            console.log("ERROR : ", e);
            $("#bth-search").prop("disabled", false);

        }
    });

}
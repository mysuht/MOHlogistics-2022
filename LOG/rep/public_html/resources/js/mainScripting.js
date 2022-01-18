$(document).ready(function () {
    // ---------------------------
    if ($('input[name=dat]:checked').val() == "m") {
        //  alert("changed");
        $("#mon").show();
        $("#quart").hide();
        $("#ud").hide();
        $("#y").show();
        $("#p_hq").hide();

    }

    if ($('input[name=dat]:checked').val() == "q") {
        // alert(" "+$("#dat").val());
        //alert("not changed");
        $("#mon").hide();
        $("#quart").show();
        $("#ud").hide();
        $("#y").show();
        $("#p_hq").hide();

    }

    if ($('input[name=dat]:checked').val() == "hq") {
        // alert(" "+$("#dat").val());
        //alert("not changed");
        $("#mon").hide();
        $("#quart").hide();
        $("#ud").hide();
        $("#y").show();
        $("#p_hq").show();

    }

    if ($('input[name=dat]:checked').val() == "u") {
        // alert(" "+$("#dat").val());
        //alert("not changed");
        $("#mon").hide();
        $("#quart").hide();
        $("#ud").show();
        $("#y").hide();
        $("#p_hq").hide();

    }

    $('input:radio').change(function () {
        if ($('input[name=dat]:checked').val() == "u") {
            $("#mon").hide();
            $("#quart").hide();
            $("#ud").show();
            $("#y").hide();
            $("#p_hq").hide();
        }

        if ($('input[name=dat]:checked').val() == "m") {
            // alert("changed");
            $("#mon").show();
            $("#quart").hide();
            $("#ud").hide();
            $("#y").show();
            $("#p_hq").hide();
        }

        if ($('input[name=dat]:checked').val() == "q") {
            // alert(" "+$("#dat").val());
            //alert("not changed");
            $("#mon").hide();
            $("#quart").show();
            $("#ud").hide();
            $("#y").show();
            $("#p_hq").hide();

        }

        if ($('input[name=dat]:checked').val() == "hq") {
            // alert(" "+$("#dat").val());
            //alert("not changed");
            $("#mon").hide();
            $("#quart").hide();
            $("#ud").hide();
            $("#y").show();
            $("#p_hq").show();

        }

    });
    var rep = $("#rep").val();

    $('#p_dir option[value=' + $("#pd").val() + ']').attr('selected', 'selected');
    $('#rep option[value=' + $("#rep").val() + ']').attr('selected', 'selected');

    ////////////rep changed
    var repVal = "";
    $("#rep").change(function () {
        var comments = "";
        $(".cmt").remove();
        if ($("#rep").val() == "24") {
            comments = "This ' New Version ' is get the facility, that matchs the below procedures<br/>";
            comments += " for the selected producted at the same month. <br/>";
            comments = " The Report displays The Facilities that matches The following criteria:  <br/> ";
            comments += " 1. closing balance is zero <br/> ";
            comments += " 2. average monthly consumption greater than zero <br/>";
            comments += " 3. The Adjustment Type doesn't equal to the following: <br/>";
            comments += "     a. is not Transferred Out. <br/> ";
            comments += "     a. is not 'Closed Warehouse. <br/> ";
            comments += "     a. is not 'Closed SDP'. <br/> ";
            comments += " <br/>";
            comments += "All Requested Products should be revealed  <br/> ";
            comments += " at the same month even if it occurs once only in the requested period ";
            //  $("#appIn").append("<p>Use only with monthly periods</p>");
        }
        else if ($("#rep").val() == "19") {
//            comments = " the facilities with products <br/> ";
//            comments += " at least one of the selected <br/>";
//            comments += " choosen by the user, that match the below procedures<br/>";
//            comments += " for the selected products at a month in a selected period. <br/>";
            comments += " The Report displays The Facilities that matches The following criteria:  <br/> ";
            comments += " 1. closing balance is zero <br/> ";
            comments += " 2. average monthly consumption greater than zero <br/>";
            comments += " 3. The Adjustment Type doesn't equal to the following: <br/>";
            comments += "     a. is not Transferred Out. <br/> ";
            comments += "     a. is not 'Closed Warehouse. <br/> ";
            comments += "     a. is not 'Closed SDP'. <br/> ";
            
            comments += " <br/>";
            comments += "All the requested products  should be displayed even if the stockout  <br/> ";
            comments += "for each product occurs once only during the requested period ";
        }
        else if ($("#rep").val() == "18") {
            comments = "Use only with monthly periods 123";
        }
        else if ($("#rep").val() == "22") {

        }
        else if ($("#rep").val() == "7") {
            $("#comt").val("Use only with monthly periods");

            //  $("#appIn").append("<p>Use only with monthly periods</p>");
        }
        else if ($("#rep").val() == "2") {
            comments = "Choose The Facility Types and it's branches <br/>";
        }
        else if ($("#rep").val() == "12") {
           comments += " <br/>";
            comments += "All the requested products  should be displayed even if the dispensed to user  <br/> ";
            comments += "for the product occurs once only during the requested period ";
        }else if($("#rep").val() == "13"){
             comments += " <br/>";
            comments += "All the requested products  should be displayed even if the dispensed to user  <br/> ";
            comments += "for the product occurs once only during the requested period ";
        }
        else if ($("#rep").val() == "4") {
            comments = "No. Comment...  <br/> ";''
        }else if ($("#rep").val() == "25") {
            comments += " <br/>";
            comments += "All the requested products  should be displayed in condition <br/> ";
            comments += "that dispensed to user  <br/> ";
            comments += "for the products should occur in the same month of the requested period ";
        }else if ($("#rep").val() == "26") {
            comments += " <br/>";
            comments += "All requested products  should be displayed in condition <br/> ";
            comments += "that dispensed to user  <br/> ";
            comments += "for the products should occur in the same month of the requested period ";
        }
        $("#appIn").append("<p class='cmt'>" + comments + "</p>");

        $("#pd").val($("#p_dir").val());
        $("#re").val($("#rep").val());

        // window.location.href = "main.jsp?pd=" + $("#pd").val() + "&re=" + $("#rep").val() + "&comt=" + repVal + "&me=" + $("#p_mon").val() + "&ye=" + $("#dat").val();
        $("#pd").val($("#p_dir").val());
        $('#p_dir option[value=' + $("#pd").val() + ']').attr('selected', 'selected');
        $("#me").val($("#me").val());
        $("#re").val($("#rep").val());
        $('#rep option[value=' + val1 + ']').attr('selected', 'selected');
        $("#rep").val(val1);
        $('#rep option[value=' + val1 + ']').attr('selected', 'selected');
        //        alert(comments);
    });

    /////////end rep change
    $("#p_dir").change(function () {
                $("#pd").val($("#p_dir").val());
                $("#re").val($("#rep").val());
                var val1 = $("#rep").val();
        //
                window.location.href = "main.jsp?pd=" + $("#pd").val() + "&re=" + val1 + "&comt=" + repVal;
        
                $("#pd").val($("#p_dir").val());
                $('#p_dir option[value=' + $("#pd").val() + ']').attr('selected', 'selected');
        
                $("#re").val($("#rep").val());
                $('#rep option[value=' + val1 + ']').attr('selected', 'selected');
                $("#rep").val(val1);
                // *******************
//        $.ajax( {
//            type : "GET", url : "/rep/processrep", dataType : 'json', data : "type=" + $("#pd").val(), success : function (json) {
//                var myData = json.JsonData;
//                $('#p_cen').find('option').remove();
//                $.each(myData, function (index, element) {
//                    $('#p_cen').append('<option value="' + element.id + '" selected="selected">' + element.name + '</option>').multiselect("refresh");;
//                });
//                $('#p_cen').multiselect('destroy');
//                $('#p_cen').multiselect().multiselectfilter();
//               // initMultiSelect();
//            },
////            async : false,
////            global : false,
//            error : function () {
//                alert("Errr is occured");
//            }
//        });

        //$('#rep option[value='+val1+']').attr('selected','selected');
    });
    
    
});
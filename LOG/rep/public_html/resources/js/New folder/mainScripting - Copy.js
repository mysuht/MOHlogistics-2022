   
    $(document).ready(function(){
        // ---------------------------
        
         if( $('input[name=dat]:checked').val() == "m" ){
            //  alert("changed");
                  $("#mon").show();
                  $("#quart").hide();
                   $("#ud").hide();
                   $("#y").show();
                     $("#p_hq").hide();
                  
              }
              
              if($('input[name=dat]:checked').val() == "q" ){
             // alert(" "+$("#dat").val());
              //alert("not changed");
                   $("#mon").hide();
                  $("#quart").show();
                  $("#ud").hide();
                   $("#y").show();
                     $("#p_hq").hide();
                  
              }
              
               if($('input[name=dat]:checked').val() == "hq" ){
             // alert(" "+$("#dat").val());
              //alert("not changed");
                   $("#mon").hide();
                  $("#quart").hide();
                  $("#ud").hide();
                   $("#y").show();
                   $("#p_hq").show();
                  
              }
              
              
              
               if($('input[name=dat]:checked').val() == "u" ){
             // alert(" "+$("#dat").val());
              //alert("not changed");
                   $("#mon").hide();
                  $("#quart").hide();
                  $("#ud").show();
                   $("#y").hide();
                     $("#p_hq").hide();
                  
              }
           
           
           
           
              //alert("hiiiiiii") ;
              $('input:radio').change(
              function(){
               if($('input[name=dat]:checked').val() == "u" ){
             // alert(" "+$("#dat").val());
              //alert("not changed");
                   $("#mon").hide();
                  $("#quart").hide();
                  $("#ud").show();
                  $("#y").hide();
                    $("#p_hq").hide();
              }
              
             if( $('input[name=dat]:checked').val() == "m" ){
             // alert("changed");
                  $("#mon").show();
                  $("#quart").hide();
                   $("#ud").hide();
                    $("#y").show();
                      $("#p_hq").hide();
              }
              
              if($('input[name=dat]:checked').val() == "q" ){
             // alert(" "+$("#dat").val());
              //alert("not changed");
                   $("#mon").hide();
                  $("#quart").show();
                   $("#ud").hide();
                     $("#y").show();
                       $("#p_hq").hide();
                  
              }
              
               if($('input[name=dat]:checked').val() == "hq" ){
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
        
        // ---------------------------
       // window.location.replace("dataEntryErrorReportParam.jsp?pd=");
        $('#p_dir option[value='+$("#pd").val()+']').attr('selected','selected');
        $('#rep option[value='+$("#rep").val()+']').attr('selected','selected');
        var dirO = $("#p_dir").val();
        var dirOld = $("#pd").val();
        
        ////////////rep changed
        var repVal="";
        $("#rep").change(function(){
        
        if($("#rep").val() == "22"){
        $("#comt").val("Use only with monthly periods");
        repVal = $("#comt").val();
          //  $("#appIn").append("<p>Use only with monthly periods</p>");
          
        }
        
//        if($("#rep").val() == "7"){
//        $("#comt").val("For All Products");
//        repVal = $("#comt").val();
//          //  $("#appIn").append("<p>Use only with monthly periods</p>");
//          
//        }
          if($("#rep").val() == "7"){
        $("#comt").val("Use only with monthly periods");
        repVal = $("#comt").val();
          //  $("#appIn").append("<p>Use only with monthly periods</p>");
          
        }
         if($("#rep").val() == "18"){
        $("#comt").val("Use only with monthly periods");
        repVal = $("#comt").val();
          //  $("#appIn").append("<p>Use only with monthly periods</p>");
          
        }
//        if($("#rep").val() == "7"){
//        $("#comt").val("For All Products");
//        repVal = $("#comt").val();
//          //  $("#appIn").append("<p>Use only with monthly periods</p>");
//          
//        }
        
        if($("#rep").val() == "2"){
        $("#comt").val("Choose The Facility Types and it's branches");
        repVal = $("#comt").val();
          //  $("#appIn").append("<p>Use only with monthly periods</p>");
          
        }
        
        
        if($("#rep").val() == "12"){
        $("#comt").val("Select multiple products");
        repVal = $("#comt").val();
          //  $("#appIn").append("<p>Use only with monthly periods</p>");
          
        }
        
        else if($("#rep").val() == "4"){
        $("#comt").val("No. Comment...");
        repVal = $("#comt").val();
          //  $("#appIn").append("<p>Use only with monthly periods</p>");
          
        }
        
        
//        else if($("#rep").val() != "12" || $("#rep").val() != "4" || $("#rep").val() != "22" ){
//             $("#comt").val("No Commit ..........");
//        repVal = $("#comt").val();
//        }
        
         $("#pd").val($("#p_dir").val());
        $("#re").val($("#rep").val());
       // $("#f2").submit();
       window.location.href="main.jsp?pd="+$("#pd").val()+"&re="+$("#rep").val()+"&comt="+repVal;
       
       $("#pd").val($("#p_dir").val());
       $('#p_dir option[value='+$("#pd").val()+']').attr('selected','selected');
       
       $("#re").val($("#rep").val());
       $('#rep option[value='+val1+']').attr('selected','selected');
       $("#rep").val(val1);
        $('#rep option[value='+val1+']').attr('selected','selected');
        });
        
        /////////end rep change
        
        $("#p_dir").change(function(){
       // alert($("#rep").val());
        var val1 = $("#rep").val();
        
        $("#pd").val($("#p_dir").val());
        $("#re").val($("#rep").val());
       // $("#f2").submit();
       
       ///-----------------------
       if($("#rep").val() == "22"){
        $("#comt").val("Use only with monthly periods");
        repVal = $("#comt").val();
          //  $("#appIn").append("<p>Use only with monthly periods</p>");
          
        }
        if($("#rep").val() == "7"){
        $("#comt").val("Use only with monthly periods");
        repVal = $("#comt").val();
          //  $("#appIn").append("<p>Use only with monthly periods</p>");
          
        }
          if($("#rep").val() == "18"){
        $("#comt").val("Use only with monthly periods");
        repVal = $("#comt").val();
          //  $("#appIn").append("<p>Use only with monthly periods</p>");
          
        }
        else if($("#rep").val() == "4"){
        $("#comt").val("No. Comment...");
        repVal = $("#comt").val();
          //  $("#appIn").append("<p>Use only with monthly periods</p>");
          
        }
        
         if($("#rep").val() == "7"){
        $("#comt").val("For All Products");
        repVal = $("#comt").val();
          //  $("#appIn").append("<p>Use only with monthly periods</p>");
          
        }
        
         if($("#rep").val() == "2"){
        $("#comt").val("Choose The Facility Types and it's branches");
        repVal = $("#comt").val();
          //  $("#appIn").append("<p>Use only with monthly periods</p>");
          
        }
        
       else if($("#rep").val() == "12"){
        $("#comt").val("Select multiple products");
        repVal = $("#comt").val();
          //  $("#appIn").append("<p>Use only with monthly periods</p>");
          
        }
        
        
//        else if($("#rep").val() != "12" || $("#rep").val() != "4" || $("#rep").val() != "22" ){
//             $("#comt").val("No Commit ..........");
//        repVal = $("#comt").val();
//        }
        
       //---------------------
       
       window.location.href="main.jsp?pd="+$("#pd").val()+"&re="+val1+"&comt="+repVal;
       
       $("#pd").val($("#p_dir").val());
       $('#p_dir option[value='+$("#pd").val()+']').attr('selected','selected');
       
       $("#re").val($("#rep").val());
       $('#rep option[value='+val1+']').attr('selected','selected');
       $("#rep").val(val1);
        // *******************
        var dir = $("#p_dir").val();
        var name = [];
        var dir1 = [];
      //  var n = $
   
        
        // ******************
        
      //  $("#p_cen").submit();
        // $("#p_dir").val() = dirOld;
        //$("#p_cen").append('<option >'+ $("#p_dir").val() +'</option>');
        });
        
      
        $('#rep option[value='+val1+']').attr('selected','selected');
        });
        
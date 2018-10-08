var up=false;
      function toggle() {
        if(up)
        {
          up=false;
          $('#chevron').removeClass('ui-icon-caret-1-s').addClass('ui-icon-caret-1-u');
          $('#table').height('20%');
          $('#map').height('80%');
        }
        else
        {
          $('#chevron').removeClass('ui-icon-caret-1-n').addClass('ui-icon-caret-1-s');
          $('#table').height('70%');
          $('#map').height('30%');
          up=true;
      }
      }

   $( function() {
    var dialog, form,
 
      schulnummer = $( "#schulnummer" ),
      plz = $( "#plz" ),
      up = $( "#up" ),
      down = $( "#down" ),
      allFields = $( [] ).add( schulnummer ).add(plz).add(up).add(down),
      tips = $( ".validateTips" );
 
    function updateTips( t ) {
      tips
        .text( t )
        .addClass( "ui-state-highlight" );
      setTimeout(function() {
        tips.removeClass( "ui-state-highlight", 1500 );
      }, 500 );
    }
 
    function checkSNR( o, n, len ) {
      if (( o.val().length > len || o.val().length < len ) && !o.val().length==0) {
        o.addClass( "ui-state-error" );
        updateTips( n+" muss " +
          len + "-stellig und eine Zahl sein." );
        return false;
      } else {
        return true;
      }
    }
    function checkLength( o, n, min, max ) {
      if (( o.val().length > max || o.val().length < min )) {
        o.addClass( "ui-state-error" );
        updateTips( n+" muss " +
          min + "-stellig und eine Zahl sein." );
        return false;
      } else {
        return true;
      }
    }
    function checkLength2( o, n, min, max ) {
      if (( o.val().length > max || o.val().length < min ) && !o.val().length==0) {
        o.addClass( "ui-state-error" );
        updateTips( n+" muss zwischen " +
          min + " und " + max + "-stellig sein." );
        return false;
      } else {
        return true;
      }
    }
 
    function addSchool() {
      var valid = true;
      allFields.removeClass( "ui-state-error" );
 
      valid = valid && checkSNR( schulnummer, "Schulnummer", 6 );
      valid = valid && checkLength( plz, "PLZ", 5, 5 );
      valid = valid && checkLength2( up, "Upload", 3, 9 );
      valid = valid && checkLength2( down, "Download", 3, 9 );
 
      if ( valid ) {        
            $.ajax({
                url: './input.php',
                type: 'post',
                dataType: 'json', 
                data: $('form#myForm').serialize(),
                success: function (data, text) {
                    confirmDialog.dialog("open");
                    confirmDialog.dialog({ title: "Daten übertragen" });
                    confirmDialog.html("<span style='color:green;'>Daten erfolgreich übertragen.<span>");
                    popupClear();
                    dialog.dialog( "close" );
                },
                error: function (request, status, error) {
                    if(request.responseText == "OK")
                    {
                        confirmDialog.dialog("open");
                        confirmDialog.dialog({ title: "Daten übertragen" });
                        confirmDialog.html("<span style='color:green;'>Daten erfolgreich übertragen.<span>");
                        popupClear();
                        dialog.dialog( "close" );
                    }
                    else
                    {
                        confirmDialog.dialog("open");
                        confirmDialog.dialog({ title: "Fehler" });
                        confirmDialog.html("<span style='color: red;'>Datenübertragung fehlerhaft</span>");
                    }
                }
            });
      }
      return valid;
    }

    confirmDialog = $( "#confirm-dialog" ).dialog({
               autoOpen: false, 
               modal: true,
               buttons: {
                  OK: function() {$(this).dialog("close");}
               },
            });
 
    dialog = $( "#dialog-form" ).dialog({
      autoOpen: false,
      height: 800,
      width: 350,
      modal: true,
      buttons: {
        "Schule übermitteln": addSchool,
        "schliessen": function() {
          dialog.dialog( "close" );
        }
      },
      close: function() {
        form[ 0 ].reset();
        allFields.removeClass( "ui-state-error" );
      }
    });
 
    form = dialog.find( "form" ).on( "submit", function( event ) {
      event.preventDefault();
      addUser();
    });
 
modifyTable = function (interneNummer, Schulname, PLZ, Ort, Strasse, Vorwahl, Telefon, Down, Up){
$('#interneNumme').val(interneNummer);
$('#schulname').val(Schulname);
$('#plz').val(PLZ);
$('#ort').val(Ort);
$('#str').val(Strasse);
$('#vorwahl').val(Vorwahl);
$('#tel').val(Telefon);
 if(down!='n.v.') $('#down').val(Down);
 else $('#down').val(0);
if(up!='n.v.') $('#up').val(Up);
 else $('#up').val(0);
dialog.dialog( "open" ).parent().css('z-index', 3000);
}
    $( "#add_school" ).button().on( "click", function() {
      dialog.dialog( "open" ).parent().css('z-index', 3000);
    });
  } );
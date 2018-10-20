/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.karlsommer.gigabit.filehandling;

import de.karlsommer.gigabit.database.model.Schule;
import de.karlsommer.gigabit.database.repositories.SchuleRepository;
import de.micromata.opengis.kml.v_2_2_0.*;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.List;

/**
 *
 * @author karl
 */
public class JavascriptWriter {

    private String parseGeometry(Geometry geometry) {
        String returnStirng = "";
        if(geometry != null) {
            if(geometry instanceof Polygon) {
                Polygon polygon = (Polygon) geometry;
                Boundary outerBoundaryIs = polygon.getOuterBoundaryIs();
                if(outerBoundaryIs != null) {
                    LinearRing linearRing = outerBoundaryIs.getLinearRing();
                    if(linearRing != null) {
                        List<Coordinate> coordinates = linearRing.getCoordinates();
                        if(coordinates != null) {
                            for(Coordinate coordinate : coordinates) {
                                returnStirng+=parseCoordinate(coordinate);
                            }
                        }
                    }
                }
            }
        }
        return returnStirng;
    }
    private String parseCoordinate(Coordinate coordinate) {
        if(coordinate != null) {
            return "{lat: "+coordinate.getLatitude()+", lng: "+coordinate.getLongitude()+"},";
        }
        return "";
    }

    public void publishBericht()
    {

    }

    public void writeJavaScript()
    {
        final Kml kml = Kml.unmarshal(new File("./output/gadm36_DEU_2_NRW_Arnsberg.kml"));
        final Document document = (Document) kml.getFeature();
        final Folder folder = (Folder) document.getFeature().get(0);
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("<!DOCTYPE html>\n" +
                "<html>\n" +
                "    <head>\n" +
                "    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0\">\n" +
                "    <meta name=\"apple-mobile-web-app-capable\" content=\"yes\">\n" +
                "        <script src=\"lib/jQuery.js\"></script>\n" +
                "        <script src=\"lib/jquery-ui.min.js\"></script>\n" +
                "        <script src=\"lib/bootstrap-table.js\"></script>\n" +
                "        <script src=\"lib/custom.js\"></script>\n" +
                "        <script src=\"./lib/OpenLayers.js\"></script>\n" +
                "    <link rel=\"stylesheet\" href=\"lib/jquery-ui.min.css\" type=\"text/css\">\n" +
                "    <link rel=\"stylesheet\" href=\"lib/olstyle.css\" type=\"text/css\">\n" +
                "    <link rel=\"stylesheet\" href=\"lib/fontawesome.css\" type=\"text/css\">\n" +
                "    <link rel=\"stylesheet\" href=\"lib/bootstrap-table.css\" type=\"text/css\">\n" +
                "    <link rel=\"stylesheet\" href=\"lib/custom.css\" type=\"text/css\">\n" +
                "        <title>Schulen in der Bezirksregierung</title>\n" +
                "\n" +
                "        <script>\n" +
                "            var map, mappingLayer, vectorLayer, selectMarkerControl, selectedFeature;\n" +
                "\n" +
                "        function onFeatureSelect(feature) {\n" +
                "            selectedFeature = feature;\n" +
                "            popup = new OpenLayers.Popup.FramedCloud(\"tempId\", feature.geometry.getBounds().getCenterLonLat(),\n" +
                "                                     null,\n" +
                "                                     selectedFeature.attributes.salutation,\n" +
                "                                     null, true);\n" +
                "            feature.popup = popup;\n" +
                "            map.addPopup(popup);\n" +
                "        }\n" +
                "\n" +
                "        function onFeatureUnselect(feature) {\n" +
                "            map.removePopup(feature.popup);\n" +
                "            feature.popup.destroy();\n" +
                "            feature.popup = null;\n" +
                "        }   \n" +
                "\n" +
                "        function popupClear() {\n" +
                "            //alert('number of popups '+map.popups.length);\n" +
                "            while( map.popups.length ) {\n" +
                "                 map.removePopup(map.popups[0]);\n" +
                "            }\n" +
                "        }\n" +
                "\n" +
                "        function init(){\n" +
                "        var acceptingDialog = $( \"#accepting-dialog\" ).dialog({\n" +
                "              autoOpen: true,\n" +
                "              height: 300,\n" +
                "              width: 350,\n" +
                "              modal: true,\n" +
                "              closeOnEscape: false,\n" +
                "              open: function(event, ui) {\n" +
                "                    $(\".ui-dialog-titlebar-close\", ui.dialog | ui).hide();\n" +
                "                },\n" +
                "              buttons: {\n" +
                "                \"akzeptieren\": function() {\n" +
                "                  acceptingDialog.dialog( \"close\" );\n" +
                "                  initMap();\n" +
                "                }\n" +
                "              }\n" +
                "            });\n" +
                "         }\n" +
                "            \n" +
                "        function initMap(){\n" +
                "            map = new OpenLayers.Map( 'map', {theme: null});\n" +
                "            mappingLayer = new OpenLayers.Layer.OSM(\"Simple OSM Map\");\n" +
                "            var kmlLayer = new OpenLayers.Layer.Vector(\"KML\", {\n" +
                "            strategies: [new OpenLayers.Strategy.Fixed()],\n" +
                "            protocol: new OpenLayers.Protocol.HTTP({\n" +
                "                url: \"./gadm36_DEU_2_NRW_Arnsberg.kml\",\n" +
                "                format: new OpenLayers.Format.KML({\n" +
                "                    extractStyles: true, \n" +
                "                    extractAttributes: true,\n" +
                "                    maxDepth: 8\n" +
                "                })\n" +
                "        })\n" +
                "        });\n" +
                "\n" +
                "            map.addLayer(mappingLayer);\n" +
                "            map.addLayer(kmlLayer);\n" +
                "            vectorLayer = new OpenLayers.Layer.Vector(\"Vector Layer\", {projection: \"EPSG:4326\"}); \n" +
                "            selectMarkerControl = new OpenLayers.Control.SelectFeature(vectorLayer, {onSelect: onFeatureSelect, onUnselect: onFeatureUnselect});\n" +
                "            map.addControl(selectMarkerControl);\n" +
                "\n" +
                "            selectMarkerControl.activate();\n" +
                "            map.addLayer(vectorLayer);\n" +
                "            map.setCenter(\n" +
                "                new OpenLayers.LonLat(7.98426,51.3743).transform(\n" +
                "                    new OpenLayers.Projection(\"EPSG:4326\"),\n" +
                "                    map.getProjectionObject())\n" +
                "\n" +
                "                , 8\n" +
                "            );\n");
        for(Schule schule: (new SchuleRepository()).getSchoolsWithGeodata())
        {
            stringBuilder.append("placeMarker(").append(schule.getLat()).append(",").append(schule.getLng()).append(",");
            if(schule.getAnbindung_Kbit_UL() >= 1000000 && schule.getAnbindung_Kbit_DL() >= 1000000)
                stringBuilder.append("'img/marker-green.png'");
            else if(schule.getAnbindung_Kbit_UL() >= 200000 && schule.getAnbindung_Kbit_DL() >= 20000)
                stringBuilder.append("'img/marker-blue.png'");
            else if(schule.getAnbindung_Kbit_DL() == 0 && schule.getAnbindung_Kbit_UL() == 0) {
                stringBuilder.append("'img/marker-gold.png'");
            }
            else
                stringBuilder.append("'img/marker.png'");
            stringBuilder.append(",'<div class=\"info_content\"><h3>").append(schule.getName_der_Schule()).append("</h3><p>Adresse:<br>").append(schule.getStrasse_Hsnr()).append(",<br>").append(schule.getPLZ()).append(" ").append(schule.getOrt()).append("</p><p>Anbindung Download in Kbit/s:").append(schule.getAnbindung_Kbit_DL()).append("</p><p>Anbindung Upload in Kbit/s:").append(schule.getAnbindung_Kbit_UL()).append("</p><p>").append(getOpenDialogButton(schule,"\\'")).append("</p></div>'); \n");
        }
                stringBuilder.append( "        }\n" +
                "\n" +
                "        function placeMarker(lat,lon,marker, text){\n" +
                "            var LonLat = new OpenLayers.Geometry.Point( lon, lat).transform(\"EPSG:4326\", map.getProjectionObject());\n" +
                "            var feature = new OpenLayers.Feature.Vector(LonLat,\n" +
                "                                    { salutation: text, Lon : lon, Lat : lat},\n" +
                "            {externalGraphic: marker, graphicHeight: 25, graphicWidth: 21, graphicXOffset:-12, graphicYOffset:-25  });\n" +
                "\n" +
                "            vectorLayer.addFeatures(feature);\n" +
                "            var popup = new OpenLayers.Popup.FramedCloud(\"tempId\", new OpenLayers.LonLat( lon, lat).transform(\"EPSG:4326\", map.getProjectionObject()),\n" +
                "                       null,\n" +
                "                       feature.attributes.salutation ,\n" +
                "                       null, true);\n" +
                "            feature.popup = popup;\n" +
                "        }\n" +
                "\n" +
                "        </script>\n" +
                "    </head>\n" +
                "    <body onload=\"init()\">\n" +
                "<div id=\"confirm-dialog\" title=\"Daten &uuml;bertragen\"><span style=\"color:red;\">Daten erfolgreich übertragen.<span></div>\n" +
                "        <div id=\"accepting-dialog\" title = \"Einverst&auml;ndniserkl&auml;rung\">Hiermit best&auml;tige ich, die dargestellten Informationen ausschließlich f&uuml;r den dienstlichen Gebrauch zu nutzen und nicht an Dritte weiterzugeben.</div>\n" +
                "\n" +
                "<div id=\"dialog-form\" title=\"Neue Schule hinzuf&uuml;gen\">\n" +
                "  <p class=\"validateTips\">Es ist notwendig alle Felder auszuf&uuml;llen.</p>\n" +
                " \n" +
                "  <form id=\"myForm\">\n" +
                "    <fieldset>\n" +
                "      <input type=\"hidden\" name=\"interneNumme\" id=\"interneNumme\" value=\"0\">\n" +
                "      <label for=\"schulnummer\">Schulnummer</label>\n" +
                "      <input type=\"number\" name=\"schulnummer\" id=\"schulnummer\" value=\"\" class=\"text ui-widget-content ui-corner-all\">\n" +
                "      <label for=\"schulname\">Schulname</label>\n" +
                "      <input type=\"text\" name=\"schulname\" id=\"schulname\" value=\"\" class=\"text ui-widget-content ui-corner-all\">\n" +
                "      <label for=\"plz\">PLZ</label>\n" +
                "      <input type=\"number\" name=\"plz\" id=\"plz\" value=\"\" class=\"text ui-widget-content ui-corner-all\">\n" +
                "      <label for=\"ort\">Ort</label>\n" +
                "      <input type=\"text\" name=\"ort\" id=\"ort\" value=\"\" class=\"text ui-widget-content ui-corner-all\">\n" +
                "      <label for=\"str\">Strasse und Hausnummer</label>\n" +
                "      <input type=\"text\" name=\"str\" id=\"str\" value=\"\" class=\"text ui-widget-content ui-corner-all\">\n" +
                "      <label for=\"vorwahl\">Vorwahl</label>\n" +
                "      <input type=\"number\" name=\"vorwahl\" id=\"vorwahl\" value=\"\" class=\"text ui-widget-content ui-corner-all\">\n" +
                "      <label for=\"tel\">Telefonnummer</label>\n" +
                "      <input type=\"number\" name=\"tel\" id=\"tel\" value=\"\" class=\"text ui-widget-content ui-corner-all\">\n" +
                "      <label for=\"down\">Download in KBit/s</label>\n" +
                "      <input type=\"number\" name=\"down\" id=\"down\" value=\"\" class=\"text ui-widget-content ui-corner-all\">\n" +
                "      <label for=\"up\">Upload in KBit/s</label>\n" +
                "      <input type=\"number\" name=\"up\" id=\"up\" value=\"\" class=\"text ui-widget-content ui-corner-all\">\n" +
                " \n" +
                "      <!-- Allow form submission with keyboard without duplicating the dialog button -->\n" +
                "      <input type=\"submit\" tabindex=\"-1\" style=\"position:absolute; top:-1000px\">\n" +
                "    </fieldset>\n" +
                "  </form>\n" +
                "</div>"+
                "    <div id=\"map\"></div>\n" +
                "    <div id=\"table\">\n" +
                "    <table class=\"table\" data-toggle=\"table\" data-classes=\"table table-hover table-condensed\" data-striped=\"true\"><thead>\n" +
                "    <tr>\n" +
                "      <th>Ver&auml;ndern</th>\n"+
                "      <th data-field=\"Schulname\" data-sortable=\"true\">Schulname</th>\n" +
                "      <th data-field=\"PLZ\" data-sortable=\"true\">PLZ</th>\n" +
                "      <th data-field=\"Ort\" data-sortable=\"true\">Ort</th>\n" +
                "      <th data-field=\"Str\" data-sortable=\"true\">Str. und Hausnr</th>\n" +
                "      <th data-field=\"Vorwahl\" data-sortable=\"true\">Vorwahl</th>\n" +
                "      <th data-field=\"Telefon\" data-sortable=\"true\">Telefonnummer</th>\n" +
                "      <th data-field=\"Download\" data-sortable=\"true\">Download in KBit/s</th>\n" +
                "      <th data-field=\"Upload\" data-sortable=\"true\">Upload in KBit/s</th>\n" +
                "    </tr>\n" +
                "  </thead>\n" +
                "  <tbody>\n");
        for(Schule schule: (new SchuleRepository()).getSchoolsWithGeodata()) {
            stringBuilder.append("<tr>\n").append("<td>").append(getOpenDialogButton(schule,"\'")).append("</td>\n");
            stringBuilder.append("<td>").append(schule.getName_der_Schule()).append("</td>\n");
            stringBuilder.append("<td>").append(schule.getPLZ()).append("</td>\n");
            stringBuilder.append("<td>").append(schule.getOrt()).append("</td>\n");
            stringBuilder.append("<td>").append(schule.getStrasse_Hsnr()).append("</td>\n");
            stringBuilder.append("<td>0").append(schule.getVorwahl()).append("</td><td>").append(schule.getRufnummer()).append("</td>\n");
            if(schule.getAnbindung_Kbit_DL() == 0)
                stringBuilder.append("<td>").append("n.v.");
            else
                stringBuilder.append("<td>").append(String.valueOf(schule.getAnbindung_Kbit_DL()));
            stringBuilder.append("</td>\n");
            if(schule.getAnbindung_Kbit_UL() == 0)
                stringBuilder.append("<td>").append("n.v.");
            else
                stringBuilder.append("<td>").append(String.valueOf(schule.getAnbindung_Kbit_UL()));
            stringBuilder.append("</td>\n" +
                    "</tr>\n");
        }
        stringBuilder.append("</tbody>\n" +
                "</table>"+
                "      <div controlheight=\"153\" style=\"margin: 10px; user-select: none; position: absolute; bottom: 30px; right: 10px;\">\n" +
                "      <div style=\"box-shadow: rgba(0, 0, 0, 0.3) 0px 1px 4px -1px; border-radius: 2px; cursor: pointer; background-color: rgb(255, 255, 255); width: 40px; height: 80px;\">\n" +
                "        <button id=\"toggleButton\" title=\"Vergrößern\" aria-label=\"Vergrößern\" onClick=\"toggle()\" type=\"button\" style=\"background: none; display: block; border: 0px; margin: 0px; padding: 0px; position: relative; cursor: pointer; user-select: none; overflow: hidden; width: 40px; height: 40px; top: 0px; left: 0px;\">\n" +
                "<span id=\"chevron\" class=\"ui-icon ui-icon-caret-1-n\"></span>\n" +
                "        </button>\n" +
                "        <button id=\"add_school\" title=\"Vergrößern\" aria-label=\"Vergrößern\" type=\"button\" style=\"background: none; display: block; border: 0px; margin: 0px; padding: 0px; position: relative; cursor: pointer; user-select: none; overflow: hidden; width: 40px; height: 40px; top: 0px; left: 0px;\">\n" +
                "<i style=\"font-size:10pt\">hinzu-<br />f&uuml;gen</i>\n" +
                "        </button>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "    </div>\n" +
                "  </body>\n" +
                "</html>");
        try{
            FileUtils.writeStringToFile(new File("output/output.html"), stringBuilder.toString());
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
        }
    }
    
    public void writeJavaScriptOld()
    {
        final Kml kml = Kml.unmarshal(new File("/Users/karl/NetBeansProjects/JSIandGMC/gadm36_DEU_2_NRW_Arnsberg.kml"));
        final Document document = (Document) kml.getFeature();
        final Folder folder = (Folder) document.getFeature().get(0);
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("<!DOCTYPE html>\n" +
                "<html>\n" +
                "  <head>\n" +
                "    <title>Custom Markers</title>\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, user-scalable=no\">\n" +
                "    <meta charset=\"utf-8\">\n" +
                "    <script src=\"https://code.jquery.com/jquery-3.3.1.min.js\"   integrity=\"sha256-FgpCb/KJQlLNfOu91ta32o/NMZxltwRo8QtmkMRdAu8=\"   crossorigin=\"anonymous\"></script>\n" +
                        "    <link rel=\"stylesheet\" href=\"https://code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css\">\n" +
                        "  <script src=\"https://code.jquery.com/ui/1.12.1/jquery-ui.js\"></script>\n" +
                "\n" +
                "    <!-- Latest compiled and minified CSS -->\n" +
                "<link rel=\"stylesheet\" href=\"https://cdnjs.cloudflare.com/ajax/libs/bootstrap-table/1.12.1/bootstrap-table.min.css\">\n" +
                "\n" +
                "<!-- Latest compiled and minified JavaScript -->\n" +
                "<script src=\"https://cdnjs.cloudflare.com/ajax/libs/bootstrap-table/1.12.1/bootstrap-table.min.js\"></script>\n" +
                "\n" +
                "<!-- Latest compiled and minified Locales -->\n" +
                "<script src=\"https://cdnjs.cloudflare.com/ajax/libs/bootstrap-table/1.12.1/locale/bootstrap-table-zh-CN.min.js\"></script>\n" +
                "<link rel=\"stylesheet\" href=\"https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css\">\n" +
                "    <style>\n" +
                "      /* Always set the map height explicitly to define the size of the div\n" +
                "       * element that contains the map. */\n" +
                "      #map {\n" +
                "        height: 90%;\n" +
                "      }\n" +
                "      #table {\n" +
                "       margin-right:80px;"+
                "        height:10%;\n" +
                "        overflow-y: scroll;\n" +
                "      }\n" +
                "\n" +
                "      /* Optional: Makes the sample page fill the window. */\n" +
                "      html, body {\n" +
                "        height: 100%;\n" +
                "        min-height: 100%;\n" +
                "      }\n" +
                "   label, input { display:block; }\n" +
                "    input.text { margin-bottom:12px; width:95%; padding: .4em; }\n" +
                "    fieldset { padding:0; border:0; margin-top:25px; }\n" +
                "    h1 { font-size: 1.2em; margin: .6em 0; }\n" +
                "    .ui-dialog .ui-state-error { padding: .3em; }\n" +
                "    .validateTips { border: 1px solid transparent; padding: 0.3em; }"+
                "    </style>\n" +
                "    <script type=\"text/javascript\">\n" +
                "      var up=false;\n" +
                "      function toggle() {\n" +
                "        if(up)\n" +
                "        {\n" +
                "          up=false;\n" +
                "          $('#toggleButton').html('<i class=\"fa fa-chevron-up\" style=\"font-size:24px\"></i>')\n" +
                "          $('#table').height('10%');\n" +
                "          $('#map').height('90%');\n" +
                "        }\n" +
                "        else\n" +
                "        {\n" +
                "          $('#toggleButton').html('<i class=\"fa fa-chevron-down\" style=\"font-size:24px\"></i>')\n" +
                "          $('#table').height('60%');\n" +
                "          $('#map').height('40%');\n" +
                "          up=true;\n" +
                "      }\n" +
                "      }\n" +
                "    </script>\n" +
                "<script>"+
                "$( function() {\n" +
                "    var dialog, form,\n" +
                " \n" +
                "      schulnummer = $( \"#schulnummer\" ),\n" +
                "      plz = $( \"#plz\" ),\n" +
                "      up = $( \"#up\" ),\n" +
                "      down = $( \"#down\" ),\n" +
                "      allFields = $( [] ).add( schulnummer ).add(plz).add(up).add(down),\n" +
                "      tips = $( \".validateTips\" );\n" +
                " \n" +
                "    function updateTips( t ) {\n" +
                "      tips\n" +
                "        .text( t )\n" +
                "        .addClass( \"ui-state-highlight\" );\n" +
                "      setTimeout(function() {\n" +
                "        tips.removeClass( \"ui-state-highlight\", 1500 );\n" +
                "      }, 500 );\n" +
                "    }\n" +
                " \n" +
                "    function checkSNR( o, n, len ) {\n" +
                "      if (( o.val().length > len || o.val().length < len ) && !o.val().length==0) {\n" +
                "        o.addClass( \"ui-state-error\" );\n" +
                "        updateTips( n+\" muss \" +\n" +
                "          len + \"-stellig und eine Zahl sein.\" );\n" +
                "        return false;\n" +
                "      } else {\n" +
                "        return true;\n" +
                "      }\n" +
                "    }\n" +
                "    function checkLength( o, n, min, max ) {\n" +
                "      if (( o.val().length > max || o.val().length < min )) {\n" +
                "        o.addClass( \"ui-state-error\" );\n" +
                "        updateTips( n+\" muss \" +\n" +
                "          min + \"-stellig und eine Zahl sein.\" );\n" +
                "        return false;\n" +
                "      } else {\n" +
                "        return true;\n" +
                "      }\n" +
                "    }\n" +
                "    function checkLength2( o, n, min, max ) {\n" +
                "      if (( o.val().length > max || o.val().length < min ) && !o.val().length==0) {\n" +
                "        o.addClass( \"ui-state-error\" );\n" +
                "        updateTips( n+\" muss zwischen \" +\n" +
                "          min + \" und \" + max + \"-stellig sein.\" );\n" +
                "        return false;\n" +
                "      } else {\n" +
                "        return true;\n" +
                "      }\n" +
                "    }\n" +
                " \n" +
                "    function addSchool() {\n" +
                "      var valid = true;\n" +
                "      allFields.removeClass( \"ui-state-error\" );\n" +
                " \n" +
                "      valid = valid && checkSNR( schulnummer, \"Schulnummer\", 6 );\n" +
                "      valid = valid && checkLength( plz, \"PLZ\", 5, 5 );\n" +
                "      valid = valid && checkLength2( up, \"Upload\", 3, 9 );\n" +
                "      valid = valid && checkLength2( down, \"Download\", 3, 9 );\n" +
                " \n" +
                "      if ( valid ) {\n" +
                "        $( \"#users tbody\" ).append( \"<tr>\" +\n" +
                "          \"<td>\" + schulnummer.val() + \"</td>\" +\n" +
                "          \"<td>\" + plz.val() + \"</td>\" +\n" +
                "          \"<td>\" + up.val() + \"</td>\" +\n" +
                "          \"<td>\" + down.val() + \"</td>\" +\n" +
                "        \"</tr>\" );\n" +
                "        dialog.dialog( \"close\" );\n" +
                "      }\n" +
                "      return valid;\n" +
                "    }\n" +
                " \n" +
                "    dialog = $( \"#dialog-form\" ).dialog({\n" +
                "      autoOpen: false,\n" +
                "      height: 800,\n" +
                "      width: 350,\n" +
                "      modal: true,\n" +
                "      buttons: {\n" +
                "        \"Schule übermitteln\": addSchool,\n" +
                "        \"schliessen\": function() {\n" +
                "          dialog.dialog( \"close\" );\n" +
                "        }\n" +
                "      },\n" +
                "      close: function() {\n" +
                "        form[ 0 ].reset();\n" +
                "        allFields.removeClass( \"ui-state-error\" );\n" +
                "      }\n" +
                "    });\n" +
                " \n" +
                "    form = dialog.find( \"form\" ).on( \"submit\", function( event ) {\n" +
                "      event.preventDefault();\n" +
                "      addUser();\n" +
                "    });\n" +
                " \n" +
                "modifyTable = function (interneNummer, Schulname, PLZ, Ort, Strasse, Vorwahl, Telefon, Down, Up){\n" +
                "$('#interneNumme').val(interneNummer);\n" +
                "$('#schulname').val(Schulname);\n" +
                "$('#plz').val(PLZ);\n" +
                "$('#ort').val(Ort);\n" +
                "$('#str').val(Strasse);\n" +
                "$('#vorwahl').val(Vorwahl);\n" +
                "$('#tel').val(Telefon);\n" +
                " if(down!='n.v.') $('#down').val(Down);\n" +
                " else $('#down').val(0);\n" +
                "if(up!='n.v.') $('#up').val(Up);\n" +
                " else $('#up').val(0);\n" +
                "dialog.dialog( \"open\" );\n" +
                "}\n"+
                "    $( \"#add_school\" ).button().on( \"click\", function() {\n" +
                "      dialog.dialog( \"open\" );\n" +
                "    });\n" +
                "  } );"+
                "</script>"+
                "  </head>\n" +
                "  <body>\n" +
                "<div id=\"dialog-form\" title=\"Neue Schule hinzuf&uuml;gen\">\n" +
                "  <p class=\"validateTips\">Es ist notwendig alle Felder auszuf&uuml;llen.</p>\n" +
                " \n" +
                "  <form>\n" +
                "    <fieldset>\n" +
                "      <input type=\"hidden\" name=\"interneNumme\" id=\"interneNumme\" value=\"0\">\n" +
                "      <label for=\"schulnummer\">Schulnummer</label>\n" +
                "      <input type=\"number\" name=\"schulnummer\" id=\"schulnummer\" value=\"\" class=\"text ui-widget-content ui-corner-all\">\n" +
                "      <label for=\"schulname\">Schulname</label>\n" +
                "      <input type=\"text\" name=\"schulname\" id=\"schulname\" value=\"\" class=\"text ui-widget-content ui-corner-all\">\n" +
                "      <label for=\"plz\">PLZ</label>\n" +
                "      <input type=\"number\" name=\"plz\" id=\"plz\" value=\"\" class=\"text ui-widget-content ui-corner-all\">\n" +
                "      <label for=\"ort\">Ort</label>\n" +
                "      <input type=\"text\" name=\"ort\" id=\"ort\" value=\"\" class=\"text ui-widget-content ui-corner-all\">\n" +
                "      <label for=\"str\">Strasse und Hausnummer</label>\n" +
                "      <input type=\"text\" name=\"str\" id=\"str\" value=\"\" class=\"text ui-widget-content ui-corner-all\">\n" +
                "      <label for=\"vorwahl\">Vorwahl</label>\n" +
                "      <input type=\"number\" name=\"vorwahl\" id=\"vorwahl\" value=\"\" class=\"text ui-widget-content ui-corner-all\">\n" +
                "      <label for=\"tel\">Telefonnummer</label>\n" +
                "      <input type=\"number\" name=\"tel\" id=\"tel\" value=\"\" class=\"text ui-widget-content ui-corner-all\">\n" +
                "      <label for=\"down\">Download in KBit/s</label>\n" +
                "      <input type=\"number\" name=\"down\" id=\"down\" value=\"\" class=\"text ui-widget-content ui-corner-all\">\n" +
                "      <label for=\"up\">Upload in KBit/s</label>\n" +
                "      <input type=\"number\" name=\"up\" id=\"up\" value=\"\" class=\"text ui-widget-content ui-corner-all\">\n" +
                " \n" +
                "      <!-- Allow form submission with keyboard without duplicating the dialog button -->\n" +
                "      <input type=\"submit\" tabindex=\"-1\" style=\"position:absolute; top:-1000px\">\n" +
                "    </fieldset>\n" +
                "  </form>\n" +
                "</div>"+
                "    <div id=\"map\"></div>\n" +
                "    <div id=\"table\">\n" +
                "    <table data-toggle=\"table\">"+
                "<thead>\n" +
                "    <tr>\n" +
                        "      <th>Ver&auml;ndern</th>\n"+
                "      <th data-field=\"Schulname\" data-sortable=\"true\">Schulname</th>\n" +
                "      <th data-field=\"PLZ\" data-sortable=\"true\">PLZ</th>\n" +
                "      <th data-field=\"Ort\" data-sortable=\"true\">Ort</th>\n" +
                "      <th data-field=\"Str\" data-sortable=\"true\">Str. und Hausnr</th>\n" +
                "      <th data-field=\"Vorwahl\" data-sortable=\"true\">Vorwahl</th>\n" +
                "      <th data-field=\"Telefon\" data-sortable=\"true\">Telefonnummer</th>\n" +
                "      <th data-field=\"Download\" data-sortable=\"true\">Download in KBit/s</th>\n" +
                "      <th data-field=\"Upload\" data-sortable=\"true\">Upload in KBit/s</th>\n" +
                "    </tr>\n" +
                "  </thead>\n" +
                "  <tbody>\n");
        for(Schule schule: (new SchuleRepository()).getSchoolsWithGeodata()) {
            stringBuilder.append("<tr>\n").append("<td>").append(getOpenDialogButton(schule,"\'")).append("</td>\n");
            stringBuilder.append("<td>").append(schule.getName_der_Schule()).append("</td>\n");
            stringBuilder.append("<td>").append(schule.getPLZ()).append("</td>\n");
            stringBuilder.append("<td>").append(schule.getOrt()).append("</td>\n");
            stringBuilder.append("<td>").append(schule.getStrasse_Hsnr()).append("</td>\n");
            stringBuilder.append("<td>").append(schule.getVorwahl()).append("</td><td>").append(schule.getRufnummer()).append("</td>\n");
                    if(schule.getAnbindung_Kbit_DL() == 0)
                        stringBuilder.append("<td>").append("n.v.");
                    else
                        stringBuilder.append("<td>").append(String.valueOf(schule.getAnbindung_Kbit_DL()));
            stringBuilder.append("</td>\n");
            if(schule.getAnbindung_Kbit_UL() == 0)
                stringBuilder.append("<td>").append("n.v.");
            else
                stringBuilder.append("<td>").append(String.valueOf(schule.getAnbindung_Kbit_UL()));
            stringBuilder.append("</td>\n" +
                    "</tr>\n");
        }
        stringBuilder.append("</tbody>\n" +
                "</table>"+
                "      <div controlheight=\"153\" style=\"margin: 10px; user-select: none; position: absolute; bottom: 30px; right: 10px;\">\n" +
                "      <div style=\"box-shadow: rgba(0, 0, 0, 0.3) 0px 1px 4px -1px; border-radius: 2px; cursor: pointer; background-color: rgb(255, 255, 255); width: 40px; height: 80px;\">\n" +
                "        <button id=\"toggleButton\" title=\"Vergrößern\" aria-label=\"Vergrößern\" onClick=\"toggle()\" type=\"button\" style=\"background: none; display: block; border: 0px; margin: 0px; padding: 0px; position: relative; cursor: pointer; user-select: none; overflow: hidden; width: 40px; height: 40px; top: 0px; left: 0px;\">\n" +
                "<i class=\"fa fa-chevron-up\" style=\"font-size:24px\"></i>\n" +
                "        </button>\n" +
                "        <button id=\"add_school\" title=\"Vergrößern\" aria-label=\"Vergrößern\" type=\"button\" style=\"background: none; display: block; border: 0px; margin: 0px; padding: 0px; position: relative; cursor: pointer; user-select: none; overflow: hidden; width: 40px; height: 40px; top: 0px; left: 0px;\">\n" +
                "<i style=\"font-size:10pt\">hinzu-<br />f&uuml;gen</i>\n" +
                "        </button>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "    </div>\n" +
                "    <script>" +
"      var map;\n" +
"      function initMap() {\n" +
"        map = new google.maps.Map(document.getElementById('map'), {\n" +
"          zoom: 16,\n" +
"          streetViewControl: false,\n"+
"          center: new google.maps.LatLng(-33.91722, 151.23064),\n" +
"          mapTypeId: 'roadmap'\n" +
"        });\n" +
"      var bounds = new google.maps.LatLngBounds();\n" +
"\n" +
"        var iconBase = 'http://icons.iconarchive.com/icons/icons-land/vista-map-markers/48/';\n");

        List<Feature> placemarks = folder.getFeature();
        int i = 0;
        for(Feature placemark:placemarks) {
            MultiGeometry multiGeometry = (MultiGeometry) ((Placemark)placemark).getGeometry();
            List<Geometry> geometry = multiGeometry.getGeometry();

            stringBuilder.append("var triangleCoords").append(i).append(" = [\n");
            for (Geometry geometry1 : geometry) {
                stringBuilder.append(parseGeometry(geometry1));
            }
            stringBuilder.append("        ];" +
                    "\n" +
                    "        // Construct the polygon.\n" +
                    "        var bermudaTriangle").append(i).append(" = new google.maps.Polygon({\n" +
                    "          paths: triangleCoords").append(i).append(",\n" +
                    "          strokeColor: '#FF0000',\n" +
                    "          strokeOpacity: 0.8,\n" +
                    "          strokeWeight: 1,\n" +
             //       "          fillColor: '"+String.format("#%02x%02x%02x", 255-(i*3), i%2==0?126-i*2:126+i*2, i*3)+"',\n" +
             //       "          fillOpacity: 0.35\n" +
                    "        });\n" +
                    "        bermudaTriangle").append(i).append(".setMap(map);");
        i++;
        }
        stringBuilder.append("        var icons = {\n" +
"          blue: {\n" +
"            icon: iconBase + 'Map-Marker-Ball-Azure-icon.png'\n" +
"          },\n" +
"          red: {\n" +
"            icon: iconBase + 'Map-Marker-Ball-Pink-icon.png'\n" +
"          },\n" +
"          green: {\n" +
"            icon: iconBase + 'Map-Marker-Drawing-Pin-Left-Chartreuse-icon.png'\n" +
"          },\n" +
"          ukwn: {\n" +
                "            icon: \"data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABoAAAAaCAYAAACpSkzOAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAAJcEhZcwAADsMAAA7DAcdvqGQAAAGHSURBVEhL7ZTPK0RRFMefBVkoKxv8BYgyG+ae11sYNc29jyzewkJSsrLm//AHSCSlrOysLETJ7GwUKXo/hlIWFgrjnDffkWIyw53V+NSp2+feuefe884dpzUpO05b4rvTsVFbkaZjiVirTXEyh2V/4zbv9fPGJ7Gh8reh1dGNHuvD8t8R5aknMuqqsiE9R1qt83gBsSFO5iJDl7IWP2scvsl2upGmJPFpGPqDklYjXM47WSNlhW6MtGSG3mSTxFeT0F+ICpTHYV7vp7K90PXDZZrHSS+gaiKlS5MZmoOqn0qX0W5cUKtQNeFD7aeH0rQC1Ry484qVRGoZyj6xrwbl+0ii0Lij0Ha59rxOLtcpbnMIbZfzYKDj07d5ivX4EKbsIUk4wZ4k4WQvoVYzmLIL/1usVZPwQ56FtktiaCItF0di3EVo+3CHHaS3MWoHqjnw63+URKHOulD2KWcy7XyjJYlS4HVB//MzUjp+nIGEjKHt85DLdVdbW8bQ9pEG4CRnEq3eDI7zDgYG3HhEnQFgAAAAAElFTkSuQmCC\"\n" +
                "          }\n" +
"        };\n" +
"\n" +
"        var features = [\n");
        for(Schule schule: (new SchuleRepository()).getSchoolsWithGeodata())
        {
            stringBuilder.append("{type:");
            if(schule.getAnbindung_Kbit_UL() >= 1000000 && schule.getAnbindung_Kbit_DL() >= 1000000)
                stringBuilder.append("'green'");
            else if(schule.getAnbindung_Kbit_UL() >= 200000 && schule.getAnbindung_Kbit_DL() >= 100000)
                stringBuilder.append("'blue'");
            else if(schule.getAnbindung_Kbit_DL() == 0 && schule.getAnbindung_Kbit_UL() == 0) {
                stringBuilder.append("'ukwn'");
            }
            else
                stringBuilder.append("'red'");
            stringBuilder.append(",text:'<div class=\"info_content\"><h3>").append(schule.getName_der_Schule()).append("</h3><p>Adresse:<br>").append(schule.getStrasse_Hsnr()).append(",<br>").append(schule.getPLZ()).append(" ").append(schule.getOrt()).append("</p><p>Anbindung Download in Kbit/s:").append(schule.getAnbindung_Kbit_DL()).append("</p><p>Anbindung Upload in Kbit/s:").append(schule.getAnbindung_Kbit_UL()).append("</p><p>").append(getOpenDialogButton(schule,"\\'")).append("</p></div>',name:'").append(schule.getName_der_Schule()).append("',position:new google.maps.LatLng(").append(schule.getLat()).append(", ").append(schule.getLng()).append(")},\n");
        }
       stringBuilder.append(" ];\n" +
"\n" +
"\n" +
"        // Create markers.\n" +
"        features.forEach(function(feature) {\n" +
"        bounds.extend(feature.position);\n" +
"        var infowindow = new google.maps.InfoWindow({\n" +
"          content: feature.text\n" +
"        });\n" +
"          var marker = new google.maps.Marker({\n" +
"            position: feature.position,\n" +
"            icon: icons[feature.type].icon,\n" +
"            map: map\n" +
"          });\n" +
"          marker.addListener('click', function() {\n" +
"            //infowindow.setContent(\"feature.type\");\n" +
"                infowindow.open(map, marker);\n" +
"          });\n" +
"        map.fitBounds(bounds);\n" +
"        });\n" +
"      }\n" +
"    </script>\n" +
"    <script async defer\n" +
"    src=\"https://maps.googleapis.com/maps/api/js?key=AIzaSyBelI50Mr_wlgJNF3XDbagu0QINA65q_GY&callback=initMap\">\n" +
"    </script>\n" +
"  </body>\n" +
"</html>");
        try{
        FileUtils.writeStringToFile(new File("output/output.html"), stringBuilder.toString());
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
        }
    }

    private String getOpenDialogButton(Schule schule, String escapedQuote)
    {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("<button onClick=\"modifyTable(").append(schule.getId()).append(", ").append(escapedQuote).append(schule.getName_der_Schule()).append(escapedQuote).append(", ").append(schule.getPLZ()).append(", ").append(escapedQuote).append(schule.getOrt()).append(escapedQuote).append(", ").append(escapedQuote).append(schule.getStrasse_Hsnr()).append(escapedQuote).append(", ").append(escapedQuote).append("0").append(schule.getVorwahl()).append(escapedQuote).append(", ").append(escapedQuote).append(schule.getRufnummer()).append(escapedQuote).append(", ").append(schule.getAnbindung_Kbit_DL()).append(", ").append(schule.getAnbindung_Kbit_UL()).append(") \" class=\"ui-button ui-widget ui-corner-all\"><i style=\"font-size:10pt;\">&Auml;ndern</i></button>");
        return stringBuilder.toString();
    }
    
}

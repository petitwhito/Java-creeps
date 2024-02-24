package com.epita.creeps;

import com.epita.creeps.given.extra.Cartographer;
import com.epita.creeps.given.json.Json;
import com.epita.creeps.given.vo.Tile;
import com.epita.creeps.given.vo.geometry.Point;
import com.epita.creeps.given.vo.parameter.FireParameter;
import com.epita.creeps.given.vo.parameter.MessageParameter;
import com.epita.creeps.given.vo.report.*;
import com.epita.creeps.given.vo.response.CommandResponse;
import com.epita.creeps.given.vo.response.InitResponse;
import com.epita.creeps.given.vo.response.StatisticsResponse;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.epita.creeps.given.exception.JsonParsingException;

import com.mashape.unirest.http.exceptions.UnirestException;
import com.fasterxml.jackson.databind.ObjectMapper.*;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;


public class Program {

    public static void main(String[] args) throws UnirestException {

        try {
            String login = args[2];
            String path = "http://" + args[0] + ":" + args[1];

            HttpResponse<JsonNode> jsonResponse = Unirest.post(path + "/init/" + login).asJson();
            //System.out.println(json.getBody());

            ObjectMapper mapper = new ObjectMapper();
            InitResponse initresp = mapper.readValue(jsonResponse.getBody().toString(), InitResponse.class);

            Unirest.post(path + "/command/" + login + "/" + initresp.citizen2Id + "/move:down").asJson();
            Thread.sleep(2000);


            /* start here */

            new Thread(() -> {
                try {


                    /* end of wierd part */

                    HttpResponse<JsonNode> json11 = Unirest.post(path + "/command/" + login + "/" + initresp.citizen2Id + "/spawn:turret").asJson();
                    Thread.sleep(6000);

                    CommandResponse rr = mapper.readValue(json11.getBody().toString(), CommandResponse.class);
                    HttpResponse<JsonNode> reee = Unirest.get(path + "/report/" + rr.reportId).asJson();
                    SpawnReport retard = mapper.readValue(reee.getBody().toString(), SpawnReport.class);

                    //CommandResponse rr1 = mapper.readValue(json12.getBody().toString(), CommandResponse.class);
                    //HttpResponse<JsonNode> reee1 = Unirest.get(path + "/report/" + rr1.reportId).asJson();
                    //SpawnReport retard1 = mapper.readValue(reee1.getBody().toString(), SpawnReport.class);
                    FireParameter fff = new FireParameter(retard.unitPosition.plus(2, 2));
                    //FireParameter fff1 = new FireParameter(retard1.unitPosition.plus(2, -1));

                    rr = mapper.readValue(json11.getBody().toString(), CommandResponse.class);
                    reee = Unirest.get(path + "/report/" + rr.reportId).asJson();
                    SpawnReport srepp = mapper.readValue(reee.getBody().toString(), SpawnReport.class);

                    //rr1 = mapper.readValue(json12.getBody().toString(), CommandResponse.class);
                    //reee1 = Unirest.get(path + "/report/" + rr1.reportId).asJson();
                    //SpawnReport srepp1 = mapper.readValue(reee1.getBody().toString(), SpawnReport.class);

                    ObjectWriter oww = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false).writer();
                    String d = oww.writeValueAsString(fff);
                    //String d1 = oww.writeValueAsString(fff1);

                    int j = 0;
                    while (j < 100000) {
                        HttpResponse<JsonNode> json1111 = Unirest.post(path + "/command/" + login + "/" + initresp.citizen2Id + "/observe").asJson();
                        Thread.sleep(1000);

                        CommandResponse rr1 = mapper.readValue(json1111.getBody().toString(), CommandResponse.class);
                        HttpResponse<JsonNode> reee111 = Unirest.get(path + "/report/" + rr1.reportId).asJson();
                        ObserveReport observeReport = mapper.readValue(reee111.getBody().toString(), ObserveReport.class);
                        System.out.println(observeReport.units);
                        if (observeReport.units.size() >= 2) {
                            if (!observeReport.units.get(1).player.contains("leo.sambrook")) {
                                fff = new FireParameter(observeReport.units.get(1).position);
                            }


                            d = oww.writeValueAsString(fff);

                        }

                        Unirest.post(path + "/command/" + login + "/" + srepp.spawnedUnitId + "/fire:turret").body(d).asJson();
                        Thread.sleep(2000);

                    }

                } catch (UnirestException | InterruptedException | IOException e) {
                    throw new RuntimeException(e);

                }


            }).start();



            Unirest.post(path + "/command/" + login + "/" + initresp.citizen1Id + "/move:down").asJson();
            Thread.sleep(2000);

            int k = 0;

            k=0;
            while (k != 5) {
                HttpResponse<JsonNode> json1111 = Unirest.post(path + "/command/" + login + "/" + initresp.citizen1Id + "/observe").asJson();
                Thread.sleep(1000);

                CommandResponse rr1 = mapper.readValue(json1111.getBody().toString(), CommandResponse.class);
                HttpResponse<JsonNode> reee111 = Unirest.get(path + "/report/" + rr1.reportId).asJson();
                ObserveReport observeReport = mapper.readValue(reee111.getBody().toString(), ObserveReport.class);
                Cartographer.INSTANCE.register(observeReport);
                List<Point> i = Cartographer.INSTANCE.requestOfType(Tile.Food).toList();

                int pos_x = 0;
                int count = 0;
                if (i.size() == 0) {
                    while (count != 20) {
                        pos_x--;
                        Unirest.post(path + "/command/" + login + "/" + initresp.citizen1Id + "/move:left").asJson();
                        Thread.sleep(2000);
                        Unirest.post(path + "/command/" + login + "/" + initresp.citizen1Id + "/gather").asJson();
                        Thread.sleep(4000);
                        Unirest.post(path + "/command/" + login + "/" + initresp.citizen1Id + "/build:road").asJson();
                        Thread.sleep(2000);
                        count++;

                        json1111 = Unirest.post(path + "/command/" + login + "/" + initresp.citizen1Id + "/observe").asJson();
                        Thread.sleep(1000);
                        rr1 = mapper.readValue(json1111.getBody().toString(), CommandResponse.class);
                        reee111 = Unirest.get(path + "/report/" + rr1.reportId).asJson();
                        observeReport = mapper.readValue(reee111.getBody().toString(), ObserveReport.class);
                        Cartographer.INSTANCE.register(observeReport);
                        i = Cartographer.INSTANCE.requestOfType(Tile.Food).toList();

                        if (i != null && i.size() > 0)
                            break;
                    }
                }

                int x = i.get(0).x - observeReport.unitPosition.x;
                int y = i.get(0).y - observeReport.unitPosition.y;

                System.out.print(x);
                System.out.println(y);

                pos_x = 0;
                for (int j = 0; j < Math.abs(x); j++) {
                    if (x > 0) {
                        pos_x++;
                        Unirest.post(path + "/command/" + login + "/" + initresp.citizen1Id + "/move:right").asJson();
                    }
                    else{
                        pos_x--;
                        Unirest.post(path + "/command/" + login + "/" + initresp.citizen1Id + "/move:left").asJson();
                    }

                    Thread.sleep(2000);

                    var t = Cartographer.INSTANCE.requestTileType(observeReport.unitPosition.plus(pos_x, 0));

                    if (!t.equals(Tile.Empty) && !t.equals(Tile.Road) && !t.equals(Tile.Household)) {
                        Unirest.post(path + "/command/" + login + "/" + initresp.citizen1Id + "/gather").asJson();
                        Thread.sleep(4000);
                    }
                    Unirest.post(path + "/command/" + login + "/" + initresp.citizen1Id + "/build:road").asJson();
                    Thread.sleep(2000);
                }

                int pos_y = 0;
                for (int j = 0; j < Math.abs(y); j++) {
                    if (y > 0) {
                        pos_y++;
                        Unirest.post(path + "/command/" + login + "/" + initresp.citizen1Id + "/move:up").asJson();
                    }
                    else {
                        pos_y--;
                        Unirest.post(path + "/command/" + login + "/" + initresp.citizen1Id + "/move:down").asJson();
                    }

                    Thread.sleep(2000);

                    var t = Cartographer.INSTANCE.requestTileType(observeReport.unitPosition.plus(pos_x, pos_y));

                    if (!t.equals(Tile.Empty) && !t.equals(Tile.Road) && !t.equals(Tile.Household)) {
                        System.out.println("Gathering");
                        Unirest.post(path + "/command/" + login + "/" + initresp.citizen1Id + "/gather").asJson();
                        Thread.sleep(4000);
                    }
                    Unirest.post(path + "/command/" + login + "/" + initresp.citizen1Id + "/build:road").asJson();
                    Thread.sleep(2000);
                }

                // Go back to spawn

                for (int j = 0; j < Math.abs(y); j++) {
                    if (y < 0)
                        Unirest.post(path + "/command/" + login + "/" + initresp.citizen1Id + "/move:up").asJson();
                    else
                        Unirest.post(path + "/command/" + login + "/" + initresp.citizen1Id + "/move:down").asJson();

                    Thread.sleep(2000);
                }

                for (int j = 0; j < Math.abs(x); j++) {
                    if (x < 0)
                        Unirest.post(path + "/command/" + login + "/" + initresp.citizen1Id + "/move:right").asJson();
                    else
                        Unirest.post(path + "/command/" + login + "/" + initresp.citizen1Id + "/move:left").asJson();

                    Thread.sleep(2000);
                }

                while (count != 0)
                {
                    Unirest.post(path + "/command/" + login + "/" + initresp.citizen1Id + "/move:right").asJson();
                    Thread.sleep(2000);
                    count--;
                }

                System.out.println("Unload");
                Unirest.post(path + "/command/" + login + "/" + initresp.citizen1Id + "/unload").asJson();
                Thread.sleep(3000);

                //HttpResponse<JsonNode> json = Unirest.get(path + "/statistics").asJson();
                //StatisticsResponse map = mapper.readValue(json.getBody().toString(), StatisticsResponse.class);
                k++;
            }

            HttpResponse<JsonNode> json1111 = Unirest.post(path + "/command/" + login + "/" + initresp.citizen1Id + "/upgrade").asJson();
            Thread.sleep(1000);
            Unirest.post(path + "/command/" + login + "/" + initresp.citizen1Id + "/move:up").asJson();
            Thread.sleep(2000);
            Unirest.post(path + "/command/" + login + "/" + initresp.citizen1Id + "/move:up").asJson();
            Thread.sleep(2000);
            Unirest.post(path + "/command/" + login + "/" + initresp.citizen1Id + "/move:up").asJson();
            Thread.sleep(2000);
            Unirest.post(path + "/command/" + login + "/" + initresp.citizen1Id + "/move:up").asJson();
            Thread.sleep(2000);




            //Message//

            k = 0;
            while (k == 0)
            {
                HttpResponse<JsonNode> json = Unirest.get(path + "/statistics").asJson();
                StatisticsResponse map = mapper.readValue(json.getBody().toString(), StatisticsResponse.class);
                for(int i = 0; i < map.players.size(); i++)
                {
                    String nm = map.players.get(i).name;
                    MessageParameter m = new MessageParameter(nm, "Couscous Leo OK? !");
                    ObjectWriter oww = new ObjectMapper().writer();
                    String msg = oww.writeValueAsString(m);
                    Unirest.post(path + "/command/" + login + "/" + initresp.playerId + "/message:send").body(msg).asJson();
                    Thread.sleep(1000);
                    //Unirest.post(path + "/command/" + login + "/" + initresp.citizen1Id + "/message:fetch").asJson();
                    //Thread.sleep(1000);
                }
            }


        } catch (UnirestException | IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}

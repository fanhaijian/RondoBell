package org.rondobell.akkalas;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.cluster.client.ClusterClient;
import akka.cluster.client.ClusterClientSettings;
import com.typesafe.config.ConfigFactory;

import java.util.ArrayList;
import java.util.List;

public class AkkaQuickstart {

  public static final String RADIO_ACTOR_PATH = "/user/radio";

  public static void main(String[] args) {

    System.out.println("actor start");

    ActorSystem actorSystem = ActorSystem.create("ClientActorSystem", ConfigFactory.defaultApplication());

    // start client
    ClusterClientSettings clusterClientSettings = ClusterClientSettings.create(actorSystem);
    ActorRef client = actorSystem.actorOf(ClusterClient.props(clusterClientSettings), "client");

    List<Long> radioIdList = new ArrayList<>();

    RadioReqMsg msg = new RadioReqMsg();
    msg.setMsgId("123456789");
    msg.setFrom("x");
    msg.setEvent(AkkaBizEvent.AUDIO_ADD);
    msg.setRadioIdList(radioIdList);
    client.tell(new ClusterClient.Send(RADIO_ACTOR_PATH,  msg, true),
            ActorRef.noSender());

    System.out.println("send finish");

  }
}

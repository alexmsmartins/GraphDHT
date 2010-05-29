rmiregistry 4099&
xterm -e "java -cp graphdht-oc.jar:config:lib/openchord_1.0.5.jar:lib/log4j.jar org.graphdht.openchord.real.Init localhost 5000"&
sleep 1
xterm -e "java -cp graphdht-oc.jar:config:lib/openchord_1.0.5.jar:lib/log4j.jar org.graphdht.openchord.real.Join localhost 5000 localhost 5001"&
sleep 1
xterm -e "java -cp graphdht-oc.jar:config:lib/openchord_1.0.5.jar:lib/log4j.jar org.graphdht.openchord.real.Join localhost 5000 localhost 5002"&
sleep 1
xterm -e "java -cp graphdht-oc.jar:config:lib/openchord_1.0.5.jar:lib/log4j.jar org.graphdht.openchord.real.Join localhost 5000 localhost 5003"&
sleep 1
xterm -e "java -cp graphdht-oc.jar:config:lib/openchord_1.0.5.jar:lib/log4j.jar org.graphdht.openchord.real.Join localhost 5000 localhost 5004"&
sleep 1
xterm -e "java -cp graphdht-oc.jar:config:lib/openchord_1.0.5.jar:lib/log4j.jar org.graphdht.openchord.real.Join localhost 5000 localhost 5005"&

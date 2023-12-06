package gi.stomasayuda.pm;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.hivemq.client.mqtt.MqttClient;
import com.hivemq.client.mqtt.datatypes.MqttQos;
import com.hivemq.client.mqtt.mqtt3.Mqtt3AsyncClient;

import java.util.UUID;

public class Mqtt_activity extends AppCompatActivity {


    private Mqtt3AsyncClient client;
    private EditText editTextTopic;
    private EditText editTextMessage;
    private TextView textViewMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mqtt);

        editTextTopic = findViewById(R.id.editTextTopic);
        editTextMessage = findViewById(R.id.editTextMessage);

        textViewMessage = findViewById(R.id.textViewMessage);

        Button buttonPublish = findViewById(R.id.buttonPublish);
        buttonPublish.setOnClickListener(v -> publish());

        Button buttonSubscribe = findViewById(R.id.buttonSubscribe);
        buttonSubscribe.setOnClickListener(v -> subscribe());

        String clientId = UUID.randomUUID().toString();
        client = MqttClient.builder()
                .useMqttVersion3()
                .identifier(clientId)
                .serverHost("f4c8b85d383d4ade9fce895f83715cd8.s2.eu.hivemq.cloud")
                .serverPort(8883)
                .sslWithDefaultConfig()
                .buildAsync();

        client.connectWith()
                .simpleAuth()
                .username("esticklack")
                .password("xblaxQ@1991".getBytes())
                .applySimpleAuth()
                .send()
                .whenComplete((connAck, throwable) -> {
                    if (throwable != null) {
                        // handle failure
                        runOnUiThread(() -> Toast.makeText(getBaseContext(), "Error al conectar: " + throwable.getMessage(), Toast.LENGTH_LONG).show());
                    } else {
                        // setup subscribes or start publishing
                        runOnUiThread(() -> Toast.makeText(getBaseContext(), "Conectado exitosamente!", Toast.LENGTH_LONG).show());
                    }
                });
    }

    private void publish() {
        String topic = editTextTopic.getText().toString();
        String message = editTextMessage.getText().toString();

        client.publishWith()
                .topic(topic)
                .qos(MqttQos.AT_LEAST_ONCE)
                .payload(message.getBytes())
                .send();
    }

    private void subscribe() {
        String topic = editTextTopic.getText().toString();

        client.subscribeWith()
                .topicFilter(topic)
                .qos(MqttQos.AT_LEAST_ONCE)
                .callback(publish -> {
                    String message = new String(publish.getPayloadAsBytes());
                    runOnUiThread(() -> {
                        textViewMessage.setText(message);
                        Toast.makeText(getBaseContext(), "Mensaje recibido: " + message, Toast.LENGTH_LONG).show();
                    });
                })
                .send();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        client.disconnect();
    }
}
package cl.duoc.barriodigital.notify.listener;

import cl.duoc.barriodigital.notify.config.RabbitMQNames;
import cl.duoc.barriodigital.notify.messaging.MensajeProcesador;
import cl.duoc.barriodigital.notify.messaging.NotificacionEvento;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.io.IOException;

// Generación de PDF: comprobante de ingreso o certificado de resolución (cola q.cmd.certificate).
@Component
public class CertificateListener {

	private static final Logger log = LoggerFactory.getLogger(CertificateListener.class);

	private final MensajeProcesador mensajeProcesador;

	public CertificateListener(MensajeProcesador mensajeProcesador) {
		this.mensajeProcesador = mensajeProcesador;
	}

	@RabbitListener(queues = RabbitMQNames.QUEUE_CERTIFICATE)
	public void recibir(String mensajeJson, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) throws IOException {
		try {
			NotificacionEvento evento = mensajeProcesador.leer(mensajeJson);
			mensajeProcesador.procesar(evento, RabbitMQNames.QUEUE_CERTIFICATE);
			channel.basicAck(deliveryTag, false);
			log.info("ACK enviado");
		} catch (Exception ex) {
			log.error("ERROR: {}", ex.getMessage());
			channel.basicNack(deliveryTag, false, false);
			log.warn("NACK enviado, mensaje derivado a {}", RabbitMQNames.QUEUE_CERTIFICATE_DLQ);
		}
	}

}

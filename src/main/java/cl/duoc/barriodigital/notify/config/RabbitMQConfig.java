package cl.duoc.barriodigital.notify.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Declarables;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// Declara la misma topología que ms-barriodigital-requests: así este microservicio puede
// levantarse de forma independiente (las declaraciones son idempotentes entre microservicios).
@Configuration
public class RabbitMQConfig {

	@Bean
	public DirectExchange cmdDirectExchange() {
		return new DirectExchange(RabbitMQNames.EXCHANGE_DIRECT);
	}

	@Bean
	public TopicExchange cmdTopicExchange() {
		return new TopicExchange(RabbitMQNames.EXCHANGE_TOPIC);
	}

	@Bean
	public DirectExchange cmdDeadLetterExchange() {
		return new DirectExchange(RabbitMQNames.EXCHANGE_DLX);
	}

	private Queue colaPrincipal(String nombre, String nombreDlq) {
		return QueueBuilder.durable(nombre)
				.withArgument("x-dead-letter-exchange", RabbitMQNames.EXCHANGE_DLX)
				.withArgument("x-dead-letter-routing-key", nombreDlq)
				.withArgument("x-max-length", RabbitMQNames.MAX_LENGTH_COLA_PRINCIPAL)
				.build();
	}

	private Queue colaDlq(String nombre) {
		return QueueBuilder.durable(nombre)
				.withArgument("x-message-ttl", RabbitMQNames.DLQ_MESSAGE_TTL_MS)
				.withArgument("x-max-length", RabbitMQNames.MAX_LENGTH_DLQ)
				.build();
	}

	@Bean
	public Queue colaEmail() {
		return colaPrincipal(RabbitMQNames.QUEUE_EMAIL, RabbitMQNames.QUEUE_EMAIL_DLQ);
	}

	@Bean
	public Queue colaEmailDlq() {
		return colaDlq(RabbitMQNames.QUEUE_EMAIL_DLQ);
	}

	@Bean
	public Queue colaCrew() {
		return colaPrincipal(RabbitMQNames.QUEUE_CREW, RabbitMQNames.QUEUE_CREW_DLQ);
	}

	@Bean
	public Queue colaCrewDlq() {
		return colaDlq(RabbitMQNames.QUEUE_CREW_DLQ);
	}

	@Bean
	public Queue colaCertificate() {
		return colaPrincipal(RabbitMQNames.QUEUE_CERTIFICATE, RabbitMQNames.QUEUE_CERTIFICATE_DLQ);
	}

	@Bean
	public Queue colaCertificateDlq() {
		return colaDlq(RabbitMQNames.QUEUE_CERTIFICATE_DLQ);
	}

	@Bean
	public Binding bindingEmailDirect() {
		return BindingBuilder.bind(colaEmail()).to(cmdDirectExchange()).with(RabbitMQNames.ROUTING_KEY_EMAIL);
	}

	@Bean
	public Binding bindingEmailTopic() {
		return BindingBuilder.bind(colaEmail()).to(cmdTopicExchange()).with(RabbitMQNames.BINDING_PATTERN_EMAIL);
	}

	@Bean
	public Binding bindingCrewDirect() {
		return BindingBuilder.bind(colaCrew()).to(cmdDirectExchange()).with(RabbitMQNames.ROUTING_KEY_CREW);
	}

	@Bean
	public Binding bindingCrewTopic() {
		return BindingBuilder.bind(colaCrew()).to(cmdTopicExchange()).with("crew.#");
	}

	@Bean
	public Binding bindingCertificateDirect() {
		return BindingBuilder.bind(colaCertificate()).to(cmdDirectExchange()).with(RabbitMQNames.ROUTING_KEY_CERTIFICATE);
	}

	@Bean
	public Binding bindingCertificateTopic() {
		return BindingBuilder.bind(colaCertificate()).to(cmdTopicExchange()).with("certificate.*");
	}

	@Bean
	public Declarables bindingsDlq() {
		return new Declarables(
				BindingBuilder.bind(colaEmailDlq()).to(cmdDeadLetterExchange()).with(RabbitMQNames.QUEUE_EMAIL_DLQ),
				BindingBuilder.bind(colaCrewDlq()).to(cmdDeadLetterExchange()).with(RabbitMQNames.QUEUE_CREW_DLQ),
				BindingBuilder.bind(colaCertificateDlq()).to(cmdDeadLetterExchange()).with(RabbitMQNames.QUEUE_CERTIFICATE_DLQ)
		);
	}

}

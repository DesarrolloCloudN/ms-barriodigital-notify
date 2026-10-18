package cl.duoc.barriodigital.notify.config;

// Nombres de exchanges, colas, DLQ y routing keys de BarrioDigital (EP2).
// Debe coincidir con RabbitMQNames de ms-barriodigital-requests: ambos declaran la misma topología.
public final class RabbitMQNames {

	private RabbitMQNames() {
	}

	public static final String EXCHANGE_DIRECT = "cmd.direct";
	public static final String EXCHANGE_TOPIC = "cmd.topic";
	public static final String EXCHANGE_DLX = "cmd.dead.dlx";

	public static final String QUEUE_EMAIL = "q.cmd.email";
	public static final String QUEUE_EMAIL_DLQ = "q.cmd.email.dlq";
	public static final String ROUTING_KEY_EMAIL = "email.send";
	public static final String BINDING_PATTERN_EMAIL = "email.*";

	public static final String QUEUE_CREW = "q.cmd.crew";
	public static final String QUEUE_CREW_DLQ = "q.cmd.crew.dlq";
	public static final String ROUTING_KEY_CREW = "crew.ticket";

	public static final String QUEUE_CERTIFICATE = "q.cmd.certificate";
	public static final String QUEUE_CERTIFICATE_DLQ = "q.cmd.certificate.dlq";
	public static final String ROUTING_KEY_CERTIFICATE = "certificate.gen";

	// Valor "mágico" que el equipo usa a propósito en la descripción de un trámite para
	// forzar un error de procesamiento controlado durante la prueba empírica (demo DLQ).
	public static final String MARCA_ERROR_FORZADO = "FORZAR_ERROR_DEMO";

	// Retención y limpieza (Etapa 14 de la guía): deben coincidir con RabbitMQNames de
	// ms-barriodigital-requests, ya que ambos declaran las mismas colas.
	public static final int MAX_LENGTH_COLA_PRINCIPAL = 1000;
	public static final int MAX_LENGTH_DLQ = 500;
	public static final long DLQ_MESSAGE_TTL_MS = 7L * 24 * 60 * 60 * 1000; // 7 días

}

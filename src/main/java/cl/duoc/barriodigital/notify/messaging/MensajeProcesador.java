package cl.duoc.barriodigital.notify.messaging;

import cl.duoc.barriodigital.notify.config.RabbitMQNames;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

// Lógica compartida por los tres listeners: deserializar el mensaje y simular el procesamiento
// (envío de email, ticket a cuadrilla o generación de certificado). Queda separada de la
// mecánica de ACK/NACK, que es propia de cada listener porque depende de su propio Channel.
@Component
public class MensajeProcesador {

	private static final Logger log = LoggerFactory.getLogger(MensajeProcesador.class);

	private final ObjectMapper objectMapper;

	public MensajeProcesador(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

	public NotificacionEvento leer(String mensajeJson) {
		return objectMapper.readValue(mensajeJson, NotificacionEvento.class);
	}

	// Simula el trabajo real (enviar email, imprimir ticket, generar PDF). Si la descripción del
	// trámite contiene la marca de prueba, lanza una falla a propósito: es el gatillo que el
	// equipo usa en vivo para demostrar el camino de error -> DLQ sin tocar código.
	public void procesar(NotificacionEvento evento, String nombreCola) {
		log.info("Mensaje recibido / Queue: {} / tipoEvento: {} / eventId: {} / correlationId: {}",
				nombreCola, evento.tipoEvento(), evento.eventId(), evento.correlationId());

		if (evento.descripcion() != null && evento.descripcion().contains(RabbitMQNames.MARCA_ERROR_FORZADO)) {
			throw new IllegalStateException("Error de procesamiento simulado (marca " + RabbitMQNames.MARCA_ERROR_FORZADO + ")");
		}

		log.info("Procesamiento exitoso");
	}

}

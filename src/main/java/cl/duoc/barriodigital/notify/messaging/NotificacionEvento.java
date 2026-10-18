package cl.duoc.barriodigital.notify.messaging;

import java.time.LocalDateTime;

// Debe reflejar los mismos campos que NotificacionEvento en ms-barriodigital-requests:
// el mensaje viaja como JSON plano (sin type headers) para no acoplar las dos clases.
public record NotificacionEvento(
		String eventId,
		String correlationId,
		Long tramiteId,
		String vecinoId,
		String vecinoNombre,
		String descripcion,
		String estado,
		String tipoEvento,
		LocalDateTime timestamp
) {
}

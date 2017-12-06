package haur_ranking.event;

public class GUIActionEvent {
	public enum GUIActionEventType {
		CHOOSE_STAGES_TO_DELETE_BUTTON_CLICKED, DELETE_STAGES_BUTTON_CLICKED, CANCEL_DELETE_STAGES_BUTTON_CLICKED
	}

	GUIActionEventType eventType;

	public GUIActionEvent(GUIActionEventType eventType) {
		this.eventType = eventType;
	}

	public GUIActionEventType getEventType() {
		return eventType;
	}

	public void setEventType(GUIActionEventType eventType) {
		this.eventType = eventType;
	}

}

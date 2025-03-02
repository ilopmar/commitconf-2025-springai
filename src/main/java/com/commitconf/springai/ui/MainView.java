package com.commitconf.springai.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyModifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import java.util.List;
import org.springframework.web.client.RestClient;

@Route("")
@PageTitle("Demo Spring AI CommitConf 2025")
@UIScope
public class MainView extends VerticalLayout {

  private static final List<ControllerInfo> CONTROLLERS_INFO = List.of(
      new ControllerInfo("Chat", "/chat", "Cuéntame un chiste sobre informáticos que acuden a una conferencia"),
      new ControllerInfo("Chat (Raw Response)", "/chat-response", "Cuéntame un chiste sobre informáticos que acuden a una conferencia"),
      new ControllerInfo("Chat (Stream)", "", "Explícame en 5 párrafos lo que sepas sobre el lenguaje Java (curl/httpie)"),
      new ControllerInfo("Memory Chat", "/chat-memory", "¿Como me llamo?"),
      new ControllerInfo("System Prompt", "/system",
          "Cuantas 'a' hay en amapola. A ver si eres capaz de contarlas bien porque creo que no tienes ni idea"),
      new ControllerInfo("Filmografía", "/films-list",
          "Genera la filmografía de los siguientes actores y actrices: Denzel Washington, Jennifer Connelly y Tom Hanks"),
      new ControllerInfo("Filmografía actor/actriz", "/films-by-actor", "Keanu Reeves, Matt Damon, Julia Roberts"),
      new ControllerInfo("Día y hora", "/date-time", "¿Qué día es mañana?"),
      new ControllerInfo("Tiempo en ciudad", "/cities", "¿Qué tiempo hará mañana en Madrid?"),
      new ControllerInfo("RAG (Retrieval Augmented Generation)", "/nobel",
          "Dime en qué año gano el Premio Nobel de Ciencia Joan Clarke y por qué motivo"),
      new ControllerInfo("Detección en foto", "/image-to-text", "Por favor, ¿me puedes describir en la imagen?"),
      new ControllerInfo("Generación de imagen", "/text-to-image",
          "Genera una imagen de un pingüino de Linux como si lo hubieran pintado en El Renacimiento.")
  );

  private final ComboBox<ControllerInfo> controllerSelector;

  private final TextArea inputArea;

  private final TextArea responseArea;

  private final Button sendButton;

  private final RestClient restClient;

  private final ObjectMapper objectMapper;

  public MainView(RestClient.Builder restClientBuilder, ObjectMapper objectMapper) {
    this.restClient = restClientBuilder
        .baseUrl("http://localhost:8080")
        .build();
    this.objectMapper = objectMapper;

    this.controllerSelector = createControllerSelector();
    this.inputArea = createInputArea();
    this.responseArea = createResponseArea();
    this.sendButton = createSendButton();

    setupLayout();
    setupEventListeners();
  }

  private ComboBox<ControllerInfo> createControllerSelector() {
    ComboBox<ControllerInfo> selector = new ComboBox<>("Selecciona endpoint");
    selector.setItems(CONTROLLERS_INFO);
    selector.setItemLabelGenerator(ControllerInfo::name);
    selector.setWidth("300px");
    selector.getElement().executeJs("this.inputElement.setAttribute('readonly', 'true')");
    return selector;
  }

  private TextArea createInputArea() {
    TextArea area = new TextArea("Mensaje");
    area.setWidthFull();
    area.setRequired(true);
    area.setHelperText("Introduce tu mensaje para la IA. Presiona Ctrl+Enter para enviar.");
    return area;
  }

  private TextArea createResponseArea() {
    TextArea area = new TextArea("Respuesta");
    area.setWidthFull();
    area.setMinHeight("300px");
    area.setReadOnly(true);
    area.getStyle().set("font-family", "monospace");
    return area;
  }

  private Button createSendButton() {
    Button button = new Button("Enviar petición");
    button.addClickListener(event -> sendRequest());
    button.addClickShortcut(Key.ENTER, KeyModifier.CONTROL);
    button.getStyle().set("margin-top", "16px");
    return button;
  }

  private void setupLayout() {
    H1 title = new H1("Demo Spring AI CommitConf 2025");
    VerticalLayout formLayout = new VerticalLayout(
        controllerSelector,
        inputArea,
        sendButton
    );
    formLayout.setSpacing(true);
    formLayout.setPadding(true);
    formLayout.setAlignItems(Alignment.STRETCH);

    setSpacing(true);
    setMargin(true);
    setAlignItems(Alignment.CENTER);
    setJustifyContentMode(JustifyContentMode.CENTER);
    setDefaultHorizontalComponentAlignment(Alignment.CENTER);
    setMaxWidth("1000px");
    getStyle().set("margin", "0 auto");

    add(title, formLayout, responseArea);
  }

  private void setupEventListeners() {
    controllerSelector.addValueChangeListener(event -> {
      if (event.getValue() == null) {
        controllerSelector.setValue(CONTROLLERS_INFO.getFirst());
        return;
      }
      ControllerInfo selected = event.getValue();
      inputArea.setValue(selected.defaultMessage());
      responseArea.setValue("");
    });

    controllerSelector.setValue(CONTROLLERS_INFO.getFirst());
  }

  private void sendRequest() {
    ControllerInfo selectedController = controllerSelector.getValue();
    if (selectedController == null) {
      responseArea.setValue("Please select a controller first");
      return;
    }

    String message = inputArea.getValue();
    if (message == null || message.trim().isEmpty()) {
      responseArea.setValue("Please enter a message");
      return;
    }

    responseArea.setValue("Enviando petición...");
    sendButton.setEnabled(false);

    try {
      String response = restClient.get()
          .uri(uriBuilder -> uriBuilder
              .path(selectedController.endpoint())
              .queryParam("message", message)
              .build())
          .retrieve()
          .body(String.class);

      if (response != null) {
        if (isJson(response)) {
          responseArea.setValue(formatJson(response));
        } else {
          responseArea.setValue(response);
        }
      } else {
        responseArea.setValue("No response received");
      }

    } catch (Exception e) {
      String errorMessage = "Error: " + e.getMessage();
      responseArea.setValue(errorMessage);
      Notification.show(errorMessage, 5000, Notification.Position.TOP_CENTER);

    } finally {
      sendButton.setEnabled(true);
    }
  }

  private boolean isJson(String str) {
    try {
      objectMapper.readTree(str);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  private String formatJson(String json) {
    try {
      Object jsonObject = objectMapper.readValue(json, Object.class);
      return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonObject);
    } catch (Exception e) {
      return json;
    }
  }

}

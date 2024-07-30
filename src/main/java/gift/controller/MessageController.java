package gift.controller;

import gift.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

@RestController
@Tag(name = "Message Management System", description = "Operations related to message management")
public class MessageController {
    private final MessageService messageService;

    @Autowired
    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping("/send-message")
    @Operation(summary = "Send a message", description = "Sends a message to a specified receiver", tags = { "Message Management System" })
    public ResponseEntity<String> sendMessage(
            @Parameter(description = "Receiver of the message", required = true)
            @RequestParam("receiver") String receiver) {
        try {
            String response = messageService.sendMessage(receiver);
            return ResponseEntity.ok("Message sent successfully: " + response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to send message: " + e.getMessage());
        }
    }
}

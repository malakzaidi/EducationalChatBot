package com.rag.chatbot.Service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ChatAiService {
    private final ChatClient chatClient;
    private final VectorStore vectorStore;

    @Value("classpath:/prompts/prompt-template.st")
    private Resource promptResource;

    public ChatAiService(ChatClient.Builder builder, VectorStore vectorStore) {
        this.chatClient = builder.build();
        this.vectorStore = vectorStore;
    }

    public String ragChat(String request) {
        try {
            List<Document> documents = vectorStore.similaritySearch(request);
            List<String> context = documents.stream().map(Document::getContent).toList();

            PromptTemplate promptTemplate = new PromptTemplate(promptResource);
            Prompt prompt = promptTemplate.create(
                    Map.of("context", context,
                            "request", request));

            String rawResponse = chatClient.prompt(prompt).call().content();
            return formatResponseAsHtml(rawResponse);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error processing chat request: " + e.getMessage(), e);
        }
    }

    private String formatResponseAsHtml(String response) {
        return "<div style='font-family: Arial, sans-serif; line-height: 1.6;'>" +
                response
                        .trim()
                        .replaceAll("(?m)^", "<p>")
                        .replaceAll("\\n", "</p><p>")
                        .replaceAll("</p><p>$", "</p>")
                        .replace("```java", "<pre><code>")
                        .replace("```", "</code></pre>") +
                "</div>";
    }
}
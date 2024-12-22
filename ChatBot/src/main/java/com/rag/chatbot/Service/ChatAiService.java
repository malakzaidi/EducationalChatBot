package com.rag.chatbot.Service;


import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class ChatAiService {

    private ChatClient chatClient;
    private VectorStore vectorStore;
    @Value("classpath:/prompts/prompt-template.st")
    private Resource promptResource;


    public ChatAiService(ChatClient.Builder builder, VectorStore vectorStore) {
        this.chatClient = builder.build();
        this.vectorStore = vectorStore;
    }


    public String ragChat(String request) {
        // Perform similarity search in the vector store
        List<Document> documents = vectorStore.similaritySearch(request);
        List<String> context = documents.stream().map(Document::getContent).toList();

        // Load and create the prompt
        PromptTemplate promptTemplate = new PromptTemplate(promptResource);
        Prompt prompt = promptTemplate.create(
                Map.of("context", context,
                        "request", request));

        // Get the response from the chat client
        String rawResponse = chatClient.prompt(prompt).call().content();

        // Format the response
        return formatResponseAsHtml(rawResponse);
    }

    private String formatResponseAsHtml(String response) {
        return "<div style='font-family: Arial, sans-serif; line-height: 1.6;'>" +
                response
                        .trim()
                        .replaceAll("(?m)^", "<p>")  // Wrap each paragraph
                        .replaceAll("\\n", "</p><p>")  // Separate paragraphs
                        .replaceAll("</p><p>$", "</p>")  // Ensure closing tags
                        .replace("```java", "<pre><code>")
                        .replace("```", "</code></pre>") +
                "</div>";
    }

}

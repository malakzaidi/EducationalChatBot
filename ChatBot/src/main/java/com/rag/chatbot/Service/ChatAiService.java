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

    private ChatClient chatClient;
    private VectorStore vectorStore;
    @Value("classpath:/prompts/prompt-template.st")
    private Resource promptResource;

    public ChatAiService(ChatClient.Builder builder, VectorStore vectorStore) {
        this.chatClient = builder.build();
        this.vectorStore = vectorStore;
    }

    public String ragChat(String request) {
        List<Document> documents = vectorStore.similaritySearch(request);
        List<String> context = documents.stream().map(Document::getContent).toList();
        PromptTemplate promptTemplate = new PromptTemplate(promptResource);
        Prompt prompt = promptTemplate.create(
                Map.of("context", context,
                        "request", request));
        return chatClient.prompt(prompt)
                .call()
                .content();
    }

}

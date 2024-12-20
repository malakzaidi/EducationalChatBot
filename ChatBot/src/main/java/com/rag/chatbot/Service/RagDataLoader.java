package com.rag.chatbot.Service;


import jakarta.annotation.PostConstruct;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.transformer.splitter.TextSplitter;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

@Component
public class RagDataLoader {
    @Value("classpath:/pdfs/Chap1.pdf")
    private Resource pdf;
    @Value("store-data.json")
    private String store;
    private VectorStore vectorStore;


//    @Bean
    public SimpleVectorStore simpleVectorStore(EmbeddingModel embeddingModel) {
        SimpleVectorStore vectorStore = new SimpleVectorStore(embeddingModel);
        String fileStore = Path.of("src","main","resources","store")
                .toAbsolutePath() + "/" + store;

        File file = new File(fileStore);
        if (!file.exists()) {
            PagePdfDocumentReader pdfDocumentReader = new PagePdfDocumentReader(pdf);
            List<Document> documents = pdfDocumentReader.get();
            TextSplitter textSplitter = new TokenTextSplitter();
            List<Document> splitDocuments = textSplitter.split(documents);
            vectorStore.accept(splitDocuments);
            vectorStore.save(file);
        } else {
            vectorStore.load(file);
        }
        return vectorStore;
    }
//    @PostConstruct
    public void initStore() {
        PagePdfDocumentReader pdfDocumentReader = new PagePdfDocumentReader(pdf);
        List<Document> documents = pdfDocumentReader.get();
        TextSplitter textSplitter = new TokenTextSplitter();
        List<Document> splitDocuments = textSplitter.split(documents);
        vectorStore.accept(splitDocuments);
    }

//    private final SimpleVectorStore vectorStore;
//    private final TextSplitter textSplitter = new TokenTextSplitter();
//    private final String fileStore;
//
//    public RagDataLoader(EmbeddingModel embeddingModel, @Value("store-data-v1.json") String store) {
//        this.vectorStore = new SimpleVectorStore(embeddingModel);
//        this.fileStore = Path.of("src", "main", "resources", "store").toAbsolutePath() + "/" + store;
//
//        File file = new File(fileStore);
//        if (file.exists()) {
//            vectorStore.load(file); // Load existing store if available
//        }
//    }
//
//    /**
//     * Adds a new PDF document to the vector store and updates the database.
//     *
//     * @param pdfResource The PDF file uploaded by the user.
//     * @throws IOException if there's an error processing the PDF.
//     */
//    public void addPdfToVectorStore(Resource pdfResource) throws IOException {
//        // Step 1: Read PDF content
//        PagePdfDocumentReader pdfReader = new PagePdfDocumentReader(pdfResource);
//        List<Document> documents = pdfReader.get();
//
//        // Step 2: Split the content into chunks
//        List<Document> splitDocuments = textSplitter.split(documents);
//
//        // Step 3: Update vector store with embeddings
//        vectorStore.accept(splitDocuments);
//
//        // Step 4: Save the updated vector store to file
//        vectorStore.save(new File(fileStore));
//    }
//
//    public SimpleVectorStore getVectorStore() {
//        return vectorStore;
//    }
}

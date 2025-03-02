package com.commitconf.springai._06_rag;

import jakarta.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.markdown.MarkdownDocumentReader;
import org.springframework.ai.reader.markdown.config.MarkdownDocumentReaderConfig;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

@Component
class IngestionService {

  private static final Logger LOG = LoggerFactory.getLogger(IngestionService.class);

  private final VectorStore vectorStore;

  @Value("classpath:documents/mujeres_ciencia.md")
  Resource doc;

  IngestionService(VectorStore vectorStore) {
    this.vectorStore = vectorStore;
  }

  @PostConstruct
  void run() {
    LOG.info("Loading .md files as Documents");
    var markdownReader = new MarkdownDocumentReader(doc, MarkdownDocumentReaderConfig.builder().build());

    List<Document> documents = new ArrayList<>(markdownReader.get());

    LOG.info("Creating and storing Embeddings from documents...");
    vectorStore.add(new TokenTextSplitter().split(documents));
    LOG.info("Done creating embeddings");
  }

}

package com.project.hierarchy;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Slf4j
@Component
public class HierarchyManager {

    private Map<String, Node> nodes;

    @EventListener(ApplicationReadyEvent.class)
    public void loadData() throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream("files.json");
        String jsonString = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        nodes = buildHierarchy(jsonString);
    }

    public String query(String createdBy) {
        return searchNodes(nodes, createdBy)
                .stream()
                .map(node -> getPathToRoot(nodes, node))
                .toList().toString();
    }


    private String getPathToRoot(Map<String, Node> nodes, Node node) {
        String path = node.getName();
        Node parent = node;
        while (parent.getParentId() != null) {
            parent = nodes.get(parent.getParentId());
            path = parent.getName() + "/" + path;
        }
        return path;
    }

    private List<Node> searchNodes(Map<String, Node> nodes, String searchValue) {
        return nodes.values()
                .stream()
                .filter(node -> node.getCreatedBy().equals(searchValue))
                .collect(Collectors.toList());
    }

    private Map<String, Node> buildHierarchy(String jsonData) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();

        // Parse the JSON data
        JsonNode jsonNodes = objectMapper.readTree(jsonData);

        // Create a map to store nodes based on their IDs
        Map<String, Node> nodes = new HashMap<>();

        // Iterate over the JSON nodes and build the tree
        jsonNodes.forEach(jsonNode -> {
            String nodeId = jsonNode.get("id").asText();
            Node node = Node.builder()
                    .id(nodeId)
                    .parentId(jsonNode.get("parent_id").asText(null))
                    .createdBy(jsonNode.get("created_by").asText())
                    .name(jsonNode.get("name").asText())
                    .children(new ArrayList<>())
                    .build();

            nodes.put(nodeId, node);
        });

        // Append children to parents
        nodes.values().forEach(node -> {
            String parentId = node.getParentId();
            if (parentId != null) {
                nodes.get(parentId).getChildren().add(node);
            }
        });

        return nodes;
    }
}

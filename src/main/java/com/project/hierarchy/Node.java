package com.project.hierarchy;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * This class represents a single node
 */
@Data
@Builder
public class Node {
    private String id;
    private String name;
    private String parentId;
    private String createdBy;
    private List<Node> children;
}

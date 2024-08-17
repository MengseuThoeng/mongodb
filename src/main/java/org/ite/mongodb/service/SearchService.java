package org.ite.mongodb.service;

import java.util.List;
import java.util.Objects;

public interface SearchService {
    List<Object> search(String search);
}

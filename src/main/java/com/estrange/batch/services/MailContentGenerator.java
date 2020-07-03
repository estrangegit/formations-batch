package com.estrange.batch.services;

import java.io.IOException;
import com.estrange.batch.domaine.Planning;
import freemarker.template.TemplateException;

public interface MailContentGenerator {
    String generate(Planning planning) throws TemplateException, IOException;
}

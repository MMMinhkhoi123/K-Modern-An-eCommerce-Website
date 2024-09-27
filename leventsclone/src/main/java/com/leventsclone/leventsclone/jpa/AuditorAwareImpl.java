package com.leventsclone.leventsclone.jpa;

import org.springframework.data.domain.AuditorAware;
import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<String> {
    @Override
    public Optional<String> getCurrentAuditor() {
        return "Nguyen Minh Khoi".describeConstable();
    }
}

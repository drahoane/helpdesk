package cz.cvut.fel.b221.earomo.seminar.helpdesk.service;

import cz.cvut.fel.b221.earomo.seminar.helpdesk.exception.ResourceNotFoundException;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.Log;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.User;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.enumeration.LogType;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.prototype.LogPrototypeRepository;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.repository.LogRepository;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.repository.UserRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
public class LogService {
    private final LogRepository logRepository;
    private final LogPrototypeRepository logPrototypeRepository;
    private final UserRepository userRepository;

    public LogService(LogRepository logRepository, UserRepository userRepository) {
        this.logRepository = logRepository;
        this.userRepository = userRepository;
        this.logPrototypeRepository = LogPrototypeRepository.getInstance();
    }

    @Transactional
    public Log createLogByTemplate(@NotNull LogType type, @NotNull User user, @NotNull Class resource, String ip) {
        Log log = logPrototypeRepository.getPrototype(type);
        log.setUser(user);
        log.setResource(resource);
        log.setIp(ip);

        return logRepository.save(log);
    }

    @Transactional(readOnly = true)
    public Set<Log> findAllByUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException(User.class, userId));
        return new HashSet<>(logRepository.findAllByUser(user));
    }

    @Transactional(readOnly = true)
    public Log find(@NotNull Long id) {
        return logRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(Log.class, id));
    }

}

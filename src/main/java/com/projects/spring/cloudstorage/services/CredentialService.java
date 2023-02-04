package com.projects.spring.cloudstorage.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.projects.spring.cloudstorage.mappers.CredentialMapper;
import com.projects.spring.cloudstorage.models.Credential;

@Service
public class CredentialService {
    @Autowired
    private CredentialMapper credMapper;
    @Autowired
    private EncryptionService encryptionService;


    private boolean credentialExits(int credentialId) {
        return credMapper.findCredentialById(credentialId) != null;
    }

    public Credential getCredential(int credentialId) {

        Credential credentialById = credMapper.findCredentialById(credentialId);
        return credentialById;
    }

    public List<Credential> getAllCredentials() {
        return credMapper.findAllCredentials();
    }

    public List<Credential> getCredentialsByUser(int userId) {
        return credMapper.findCredentialByUser(userId);
    }

    public boolean addCredential(Credential credential) {
        String encodedKey = encryptionService.generateKey();
        String encryptedPassword = encryptionService.encryptValue(credential.getPassword(), encodedKey);
        credential.setKey(encodedKey);
        credential.setPassword(encryptedPassword);
        int created = credMapper.createCredential(credential);
        return created > 0;
    }

    public boolean updateCredential(Credential credential) {
        Credential optionalCred = credMapper.findCredentialById(credential.getCredentialId());

        if(optionalCred != null) {
            String decryptedPassword = encryptionService.decryptValue(optionalCred.getPassword(), optionalCred.getKey());
            if(!(credential.getPassword().equals(decryptedPassword))) {
                String newKey = encryptionService.generateKey();
                String newPassword = encryptionService.encryptValue(credential.getPassword(), newKey);
                optionalCred.setKey(newKey);
                optionalCred.setPassword(newPassword);
            }
            if(credential.getUrl() != null && !credential.getUrl().equals(optionalCred.getUrl())) {
                optionalCred.setUrl(credential.getUrl());
            }
            if(!credential.getUsername().isEmpty() && !credential.getUsername().equals(optionalCred.getUsername())) {
                optionalCred.setUsername(credential.getUsername());
            }
            return credMapper.updateCredential(optionalCred) > 0;
        }
        return false;
    }

    public boolean deleteCredential(int credentialId) {
        if(credentialExits(credentialId)) {
            return credMapper.deleteCredentialById(credentialId) > 0;
        }
        return false;
    }

    public String decryptPassword(Credential credential) {
        if(credential.getCredentialId() == null || credential.getKey() == null) {
            return "";
        }
        return encryptionService.decryptValue(credential.getPassword(), credential.getKey());
    }
}

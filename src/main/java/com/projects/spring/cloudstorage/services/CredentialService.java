package com.projects.spring.cloudstorage.services;

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

    public boolean updateCredential(Credential credential) {
        Credential existingCredential = credMapper.findCredentialById(credential.getCredentialId());

        if(existingCredential != null) {
            String decryptedPassword = encryptionService.decryptValue(existingCredential.getPassword(), existingCredential.getKey());
            if(!(credential.getPassword().equals(decryptedPassword))) {
                String newKey = encryptionService.generateKey();
                String newPassword = encryptionService.encryptValue(credential.getPassword(), newKey);
                existingCredential.setKey(newKey);
                existingCredential.setPassword(newPassword);
            }
            if(credential.getUrl() != null && !credential.getUrl().equals(existingCredential.getUrl())) {
                existingCredential.setUrl(credential.getUrl());
            }
            if(!credential.getUsername().isEmpty() && !credential.getUsername().equals(existingCredential.getUsername())) {
                existingCredential.setUsername(credential.getUsername());
            }
            return credMapper.updateCredential(existingCredential) > 0;
        }
        return false;
    }

    public boolean addCredential(Credential credential) {
        String newKey = encryptionService.generateKey();
        String newPassword = encryptionService.encryptValue(credential.getPassword(), newKey);
        credential.setKey(newKey);
        credential.setPassword(newPassword);
        int created = credMapper.createCredential(credential);
        return created > 0;
    }

    public boolean deleteCredential(int credentialId) {
        if(credentialExists(credentialId)) {
            return credMapper.deleteCredentialById(credentialId) > 0;
        }
        return false;
    }

    private boolean credentialExists(int credentialId) {
        return credMapper.findCredentialById(credentialId) != null;
    }

    public Credential getCredential(int credentialId) {
        Credential credentialById = credMapper.findCredentialById(credentialId);
        return credentialById;
    }

    public String decryptPassword(Credential credential) {
        if(credential.getCredentialId() == null || credential.getKey() == null) {
            return "";
        }
        return encryptionService.decryptValue(credential.getPassword(), credential.getKey());
    }
    
}

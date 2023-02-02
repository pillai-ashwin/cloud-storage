package com.projects.spring.cloudstorage.services;

import org.springframework.stereotype.Service;

import com.projects.spring.cloudstorage.models.Credential;

@Service
public class CredentialService {

    public boolean updateCredential(Credential credential) {
        return false;
    }

    public boolean addCredential(Credential credential) {
        return false;
    }

    public boolean deleteCredential(int credentialId) {
        return false;
    }

    public Credential getCredential(int credentialId) {
        return null;
    }

    public String decryptPassword(Credential credential) {
        return null;
    }
    
}

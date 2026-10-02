package com.Igor.CarSystem.service;

/**
 * Marker for the service a session works with. A {@link com.Igor.CarSystem.task.ClientSession} holds
 * an {@link AdminServiceImpl} for admins or a {@link ClientServiceImpl} for clients, and controllers
 * check which one it is.
 */
public interface Facade {

}
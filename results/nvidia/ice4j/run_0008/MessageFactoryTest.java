Here's a comprehensive test class for the provided `MessageFactory` class:

```java
import org.ice4j.attribute.Attribute;
import org.ice4j.attribute.AttributeFactory;
import org.ice4j.message.Indication;
import org.ice4j.message.Message;
import org.ice4j.message.Request;
import org.ice4j.message.Response;
import org.ice4j.transport.TransportAddress;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class MessageFactoryTest {

    @Mock
    private TransportAddress transportAddress;

    @BeforeEach
    public void setup() {
        // Setup any necessary mocks or test data here
    }

    @Test
    public void testCreateBindingRequest() {
        // Given:
        // When:
        Request bindingRequest = MessageFactory.createBindingRequest();
        // Then:
        assertNotNull(bindingRequest);
        assertEquals(Message.BINDING_REQUEST, bindingRequest.getMessageType());
    }

    @Test
    public void testCreateBindingRequest_Priority() {
        // Given:
        long priority = 10;
        // When:
        Request bindingRequest = MessageFactory.createBindingRequest(priority);
        // Then:
        assertNotNull(bindingRequest);
        assertEquals(Message.BINDING_REQUEST, bindingRequest.getMessageType());
        // Verify priority attribute is added
        Attribute priorityAttribute = bindingRequest.getAttribute(Attribute.PRIORITY_ATTRIBUTE);
        assertNotNull(priorityAttribute);
        assertEquals(priority, ((org.ice4j.attribute.PriorityAttribute) priorityAttribute).getPriority());
    }

    @Test
    public void testCreateBindingRequest_Priority_Controlling_TieBreaker() {
        // Given:
        long priority = 10;
        boolean controlling = true;
        long tieBreaker = 20;
        // When:
        Request bindingRequest = MessageFactory.createBindingRequest(priority, controlling, tieBreaker);
        // Then:
        assertNotNull(bindingRequest);
        assertEquals(Message.BINDING_REQUEST, bindingRequest.getMessageType());
        // Verify priority attribute is added
        Attribute priorityAttribute = bindingRequest.getAttribute(Attribute.PRIORITY_ATTRIBUTE);
        assertNotNull(priorityAttribute);
        assertEquals(priority, ((org.ice4j.attribute.PriorityAttribute) priorityAttribute).getPriority());
        // Verify controlling attribute is added
        Attribute controllingAttribute = bindingRequest.getAttribute(Attribute.ICE_CONTROLLING_ATTRIBUTE);
        assertNotNull(controllingAttribute);
        assertEquals(tieBreaker, ((org.ice4j.attribute.IceControllingAttribute) controllingAttribute).getTieBreaker());
    }

    @Test
    public void testCreate3489BindingResponse() {
        // Given:
        TransportAddress mappedAddress = transportAddress;
        TransportAddress sourceAddress = transportAddress;
        TransportAddress changedAddress = transportAddress;
        // When:
        Response bindingResponse = MessageFactory.create3489BindingResponse(mappedAddress, sourceAddress, changedAddress);
        // Then:
        assertNotNull(bindingResponse);
        assertEquals(Message.BINDING_SUCCESS_RESPONSE, bindingResponse.getMessageType());
        // Verify mapped address attribute is added
        Attribute mappedAddressAttribute = bindingResponse.getAttribute(Attribute.MAPPED_ADDRESS_ATTRIBUTE);
        assertNotNull(mappedAddressAttribute);
        assertEquals(mappedAddress, ((org.ice4j.attribute.MappedAddressAttribute) mappedAddressAttribute).getTransportAddress());
    }

    @Test
    public void testCreateBindingResponse() {
        // Given:
        Request request = MessageFactory.createBindingRequest();
        TransportAddress mappedAddress = transportAddress;
        // When:
        Response bindingResponse = MessageFactory.createBindingResponse(request, mappedAddress);
        // Then:
        assertNotNull(bindingResponse);
        assertEquals(Message.BINDING_SUCCESS_RESPONSE, bindingResponse.getMessageType());
        // Verify xor mapped address attribute is added
        Attribute xorMappedAddressAttribute = bindingResponse.getAttribute(Attribute.XOR_MAPPED_ADDRESS_ATTRIBUTE);
        assertNotNull(xorMappedAddressAttribute);
        assertEquals(mappedAddress, ((org.ice4j.attribute.XorMappedAddressAttribute) xorMappedAddressAttribute).getTransportAddress());
    }

    @Test
    public void testCreateBindingErrorResponse() {
        // Given:
        char errorCode = '1';
        String reasonPhrase = "Test reason phrase";
        // When:
        Response bindingErrorResponse = MessageFactory.createBindingErrorResponse(errorCode, reasonPhrase);
        // Then:
        assertNotNull(bindingErrorResponse);
        assertEquals(Message.BINDING_ERROR_RESPONSE, bindingErrorResponse.getMessageType());
        // Verify error code attribute is added
        Attribute errorCodeAttribute = bindingErrorResponse.getAttribute(Attribute.ERROR_CODE_ATTRIBUTE);
        assertNotNull(errorCodeAttribute);
        assertEquals(errorCode, ((org.ice4j.attribute.ErrorCodeAttribute) errorCodeAttribute).getErrorCode());
    }

    @Test
    public void testCreateBindingIndication() {
        // Given:
        // When:
        Indication bindingIndication = MessageFactory.createBindingIndication();
        // Then:
        assertNotNull(bindingIndication);
        assertEquals(Message.BINDING_INDICATION, bindingIndication.getMessageType());
    }

    @Test
    public void testCreateAllocateRequest() {
        // Given:
        // When:
        Request allocateRequest = MessageFactory.createAllocateRequest();
        // Then:
        assertNotNull(allocateRequest);
        assertEquals(Message.ALLOCATE_REQUEST, allocateRequest.getMessageType());
    }

    @Test
    public void testCreateAllocateRequest_Protocol_RFlag() {
        // Given:
        byte protocol = 6;
        boolean rFlag = true;
        // When:
        Request allocateRequest = MessageFactory.createAllocateRequest(protocol, rFlag);
        // Then:
        assertNotNull(allocateRequest);
        assertEquals(Message.ALLOCATE_REQUEST, allocateRequest.getMessageType());
        // Verify requested transport attribute is added
        Attribute requestedTransportAttribute = allocateRequest.getAttribute(Attribute.REQUESTED_TRANSPORT_ATTRIBUTE);
        assertNotNull(requestedTransportAttribute);
        assertEquals(protocol, ((org.ice4j.attribute.RequestedTransportAttribute) requestedTransportAttribute).getProtocol());
    }

    @Test
    public void testCreateAllocationResponse() {
        // Given:
        Request request = MessageFactory.createAllocateRequest();
        TransportAddress mappedAddress = transportAddress;
        TransportAddress relayedAddress = transportAddress;
        int lifetime = 10;
        // When:
        Response allocationResponse = MessageFactory.createAllocationResponse(request, mappedAddress, relayedAddress, lifetime);
        // Then:
        assertNotNull(allocationResponse);
        assertEquals(Message.ALLOCATE_RESPONSE, allocationResponse.getMessageType());
        // Verify xor mapped address attribute is added
        Attribute xorMappedAddressAttribute = allocationResponse.getAttribute(Attribute.XOR_MAPPED_ADDRESS_ATTRIBUTE);
        assertNotNull(xorMappedAddressAttribute);
        assertEquals(mappedAddress, ((org.ice4j.attribute.XorMappedAddressAttribute) xorMappedAddressAttribute).getTransportAddress());
    }

    @Test
    public void testCreateAllocationErrorResponse() {
        // Given:
        char errorCode = '1';
        // When:
        Response allocationErrorResponse = MessageFactory.createAllocationErrorResponse(errorCode);
        // Then:
        assertNotNull(allocationErrorResponse);
        assertEquals(Message.ALLOCATE_ERROR_RESPONSE, allocationErrorResponse.getMessageType());
        // Verify error code attribute is added
        Attribute errorCodeAttribute = allocationErrorResponse.getAttribute(Attribute.ERROR_CODE_ATTRIBUTE);
        assertNotNull(errorCodeAttribute);
        assertEquals(errorCode, ((org.ice4j.attribute.ErrorCodeAttribute) errorCodeAttribute).getErrorCode());
    }

    @Test
    public void testCreateGoogleAllocateRequest() {
        // Given:
        String username = "testUsername";
        // When:
        Request googleAllocateRequest = MessageFactory.createGoogleAllocateRequest(username);
        // Then:
        assertNotNull(googleAllocateRequest);
        assertEquals(Message.ALLOCATE_REQUEST, googleAllocateRequest.getMessageType());
        // Verify username attribute is added
        Attribute usernameAttribute = googleAllocateRequest.getAttribute(Attribute.USERNAME_ATTRIBUTE);
        assertNotNull(usernameAttribute);
        assertEquals(username, ((org.ice4j.attribute.UsernameAttribute) usernameAttribute).getUsername());
    }

    @Test
    public void testAddLongTermCredentialAttributes() {
        // Given:
        Request request = MessageFactory.createBindingRequest();
        byte[] username = "testUsername".getBytes();
        byte[] realm = "testRealm".getBytes();
        byte[] nonce = "testNonce".getBytes();
        // When:
        MessageFactory.addLongTermCredentialAttributes(request, username, realm, nonce);
        // Then:
        // Verify username attribute is added
        Attribute usernameAttribute = request.getAttribute(Attribute.USERNAME_ATTRIBUTE);
        assertNotNull(usernameAttribute);
        assertEquals(username, ((org.ice4j.attribute.UsernameAttribute) usernameAttribute).getUsername());
        // Verify realm attribute is added
        Attribute realmAttribute = request.getAttribute(Attribute.REALM_ATTRIBUTE);
        assertNotNull(realmAttribute);
        assertEquals(realm, ((org.ice4j.attribute.RealmAttribute) realmAttribute).getRealm());
        // Verify nonce attribute is added
        Attribute nonceAttribute = request.getAttribute(Attribute.NONCE_ATTRIBUTE);
        assertNotNull(nonceAttribute);
        assertEquals(nonce, ((org.ice4j.attribute.NonceAttribute) nonceAttribute).getNonce());
    }

    @Test
    public void testCreateRefreshRequest() {
        // Given:
        // When:
        Request refreshRequest = MessageFactory.createRefreshRequest();
        // Then:
        assertNotNull(refreshRequest);
        assertEquals(Message.REFRESH_REQUEST, refreshRequest.getMessageType());
    }

    @Test
    public void testCreateRefreshRequest_Lifetime() {
        // Given:
        int lifetime = 10;
        // When:
        Request refreshRequest = MessageFactory.createRefreshRequest(lifetime);
        // Then:
        assertNotNull(refreshRequest);
        assertEquals(Message.REFRESH_REQUEST, refreshRequest.getMessageType());
        // Verify lifetime attribute is added
        Attribute lifetimeAttribute = refreshRequest.getAttribute(Attribute.LIFETIME_ATTRIBUTE);
        assertNotNull(lifetimeAttribute);
        assertEquals(lifetime, ((org.ice4j.attribute.LifetimeAttribute) lifetimeAttribute).getLifetime());
    }

    @Test
    public void testCreateRefreshResponse() {
        // Given:
        int lifetime = 10;
        // When:
        Response refreshResponse = MessageFactory.createRefreshResponse(lifetime);
        // Then:
        assertNotNull(refreshResponse);
        assertEquals(Message.REFRESH_RESPONSE, refreshResponse.getMessageType());
        // Verify lifetime attribute is added
        Attribute lifetimeAttribute = refreshResponse.getAttribute(Attribute.LIFETIME_ATTRIBUTE);
        assertNotNull(lifetimeAttribute);
        assertEquals(lifetime, ((org.ice4j.attribute.LifetimeAttribute) lifetimeAttribute).getLifetime());
    }

    @Test
    public void testCreateRefreshErrorResponse() {
        // Given:
        char errorCode = '1';
        // When:
        Response refreshErrorResponse = MessageFactory.createRefreshErrorResponse(errorCode);
        // Then:
        assertNotNull(refreshErrorResponse);
        assertEquals(Message.REFRESH_ERROR_RESPONSE, refreshErrorResponse.getMessageType());
        // Verify error code attribute is added
        Attribute errorCodeAttribute = refreshErrorResponse.getAttribute(Attribute.ERROR_CODE_ATTRIBUTE);
        assertNotNull(errorCodeAttribute);
        assertEquals(errorCode, ((org.ice4j.attribute.ErrorCodeAttribute) errorCodeAttribute).getErrorCode());
    }

    @Test
    public void testCreateChannelBindRequest() {
        // Given:
        char channelNumber = '1';
        TransportAddress peerAddress = transportAddress;
        // When:
        Request channelBindRequest = MessageFactory.createChannelBindRequest(channelNumber, peerAddress, new byte[0]);
        // Then:
        assertNotNull(channelBindRequest);
        assertEquals(Message.CHANNELBIND_REQUEST, channelBindRequest.getMessageType());
        // Verify channel number attribute is added
        Attribute channelNumberAttribute = channelBindRequest.getAttribute(Attribute.CHANNEL_NUMBER_ATTRIBUTE);
        assertNotNull(channelNumberAttribute);
        assertEquals(channelNumber, ((org.ice4j.attribute.ChannelNumberAttribute) channelNumberAttribute).getChannelNumber());
    }

    @Test
    public void testCreateChannelBindResponse() {
        // Given:
        // When:
        Response channelBindResponse = MessageFactory.createChannelBindResponse();
        // Then:
        assertNotNull(channelBindResponse);
        assertEquals(Message.CHANNELBIND_RESPONSE, channelBindResponse.getMessageType());
    }

    @Test
    public void testCreateChannelBindErrorResponse() {
        // Given:
        char errorCode = '1';
        // When:
        Response channelBindErrorResponse = MessageFactory.createChannelBindErrorResponse(errorCode);
        // Then:
        assertNotNull(channelBindErrorResponse);
        assertEquals(Message.CHANNELBIND_ERROR_RESPONSE, channelBindErrorResponse.getMessageType());
        // Verify error code attribute is added
        Attribute errorCodeAttribute = channelBindErrorResponse.getAttribute(Attribute.ERROR_CODE_ATTRIBUTE);
        assertNotNull(errorCodeAttribute);
        assertEquals(errorCode, ((org.ice4j.attribute.ErrorCodeAttribute) errorCodeAttribute).getErrorCode());
    }

    @Test
    public void testCreateCreatePermissionRequest() {
        // Given:
        TransportAddress peerAddress = transportAddress;
        // When:
        Request createPermissionRequest = MessageFactory.createCreatePermissionRequest(peerAddress, new byte[0]);
        // Then:
        assertNotNull(createPermissionRequest);
        assertEquals(Message.CREATEPERMISSION_REQUEST, createPermissionRequest.getMessageType());
        // Verify xor peer address attribute is added
        Attribute xorPeerAddressAttribute = createPermissionRequest.getAttribute(Attribute.XOR_PEER_ADDRESS_ATTRIBUTE);
        assertNotNull(xorPeerAddressAttribute);
        assertEquals(peerAddress, ((org.ice4j.attribute.XorPeerAddressAttribute) xorPeerAddressAttribute).getTransportAddress());
    }

    @Test
    public void testCreateCreatePermissionResponse() {
        // Given:
        // When:
        Response createPermissionResponse = MessageFactory.createCreatePermissionResponse();
        // Then:
        assertNotNull(createPermissionResponse);
        assertEquals(Message.CREATEPERMISSION_RESPONSE, createPermissionResponse.getMessageType());
    }

    @Test
    public void testCreateCreatePermissionErrorResponse() {
        // Given:
        char errorCode = '1';
        // When:
        Response createPermissionErrorResponse = MessageFactory.createCreatePermissionErrorResponse(errorCode);
        // Then:
        assertNotNull(createPermissionErrorResponse);
        assertEquals(Message.CREATEPERMISSION_ERROR_RESPONSE, createPermissionErrorResponse.getMessageType());
        // Verify error code attribute is added
        Attribute errorCodeAttribute = createPermissionErrorResponse.getAttribute(Attribute.ERROR_CODE_ATTRIBUTE);
        assertNotNull(errorCodeAttribute);
        assertEquals(errorCode, ((org.ice4j.attribute.ErrorCodeAttribute) errorCodeAttribute).getErrorCode());
    }

    @Test
    public void testCreateSendIndication() {
        // Given:
        TransportAddress peerAddress = transportAddress;
        // When:
        Indication sendIndication = MessageFactory.createSendIndication(peerAddress, new byte[0], new byte[0]);
        // Then:
        assertNotNull(sendIndication);
        assertEquals(Message.SEND_INDICATION, sendIndication.getMessageType());
        // Verify xor peer address attribute is added
        Attribute xorPeerAddressAttribute = sendIndication.getAttribute(Attribute.XOR_PEER_ADDRESS_ATTRIBUTE);
        assertNotNull(xorPeerAddressAttribute);
        assertEquals(peerAddress, ((org.ice4j.attribute.XorPeerAddressAttribute) xorPeerAddressAttribute).getTransportAddress());
    }

    @Test
    public void testCreateDataIndication() {
        // Given:
        TransportAddress peerAddress = transportAddress;
        // When:
        Indication dataIndication = MessageFactory.createDataIndication(peerAddress, new byte[0], new byte[0]);
        // Then:
        assertNotNull(dataIndication);
        assertEquals(Message.DATA_INDICATION, dataIndication.getMessageType());
        // Verify xor peer address attribute is added
        Attribute xorPeerAddressAttribute = dataIndication.getAttribute(Attribute.XOR_PEER_ADDRESS_ATTRIBUTE);
        assertNotNull(xorPeerAddressAttribute);
        assertEquals(peerAddress, ((org.ice4j.attribute.XorPeerAddressAttribute) xorPeerAddressAttribute).getTransportAddress());
    }

    @Test
    public void testCreateSendRequest() {
        // Given:
        String username = "testUsername";
        TransportAddress peerAddress = transportAddress;
        // When:
        Request sendRequest = MessageFactory.createSendRequest(username, peerAddress, new byte[0]);
        // Then:
        assertNotNull(sendRequest);
        assertEquals(Message.SEND_REQUEST, sendRequest.getMessageType());
        // Verify username attribute is added
        Attribute usernameAttribute = sendRequest.getAttribute(Attribute.USERNAME_ATTRIBUTE);
        assertNotNull(usernameAttribute);
        assertEquals(username, ((org.ice4j.attribute.UsernameAttribute) usernameAttribute).getUsername());
    }

    @Test
    public void testCreateConnectRequest() {
        // Given:
        TransportAddress peerAddress = transportAddress;
        // When:
        Request connectRequest = MessageFactory.createConnectRequest(peerAddress, new byte[0]);
        // Then:
        assertNotNull(connectRequest);
        assertEquals(Message.CONNECT_REQUEST, connectRequest.getMessageType());
        // Verify xor peer address attribute is added
        Attribute xorPeerAddressAttribute = connectRequest.getAttribute(Attribute.XOR_PEER_ADDRESS_ATTRIBUTE);
        assertNotNull(xorPeerAddressAttribute);
        assertEquals(peerAddress, ((org.ice4j.attribute.XorPeerAddressAttribute) xorPeerAddressAttribute).getTransportAddress());
    }

    @Test
    public void testCreateConnectResponse() {
        // Given:
        int connectionIdValue = 10;
        // When:
        Response connectResponse = MessageFactory.createConnectResponse(connectionIdValue);
        // Then:
        assertNotNull(connectResponse);
        assertEquals(Message.CONNECT_RESPONSE, connectResponse.getMessageType());
        // Verify connection id attribute is added
        Attribute connectionIdAttribute = connectResponse.getAttribute(Attribute.CONNECTION_ID_ATTRIBUTE);
        assertNotNull(connectionIdAttribute);
        assertEquals(connectionIdValue, ((org.ice4j.attribute.ConnectionIdAttribute) connectionIdAttribute).getConnectionId());
    }

    @Test
    public void testCreateConnectErrorResponse() {
        // Given:
        char errorCode = '1';
        // When:
        Response connectErrorResponse = MessageFactory.createConnectErrorResponse(errorCode);
        // Then:
        assertNotNull(connectErrorResponse);
        assertEquals(Message.CONNECT_ERROR_RESPONSE, connectErrorResponse.getMessageType());
        // Verify error code attribute is added
        Attribute errorCodeAttribute = connectErrorResponse.getAttribute(Attribute.ERROR_CODE_ATTRIBUTE);
        assertNotNull(errorCodeAttribute);
        assertEquals(errorCode, ((org.ice4j.attribute.ErrorCodeAttribute) errorCodeAttribute).getErrorCode());
    }

    @Test
    public void testCreateConnectionBindRequest() {
        // Given:
        int connectionIdValue = 10;
        // When:
        Request connectionBindRequest = MessageFactory.createConnectionBindRequest(connectionIdValue);
        // Then:
        assertNotNull(connectionBindRequest);
        assertEquals(Message.CONNECTION_BIND_REQUEST, connectionBindRequest.getMessageType());
        // Verify connection id attribute is added
        Attribute connectionIdAttribute = connectionBindRequest.getAttribute(Attribute.CONNECTION_ID_ATTRIBUTE);
        assertNotNull(connectionIdAttribute);
        assertEquals(connectionIdValue, ((org.ice4j.attribute.ConnectionIdAttribute) connectionIdAttribute).getConnectionId());
    }

    @Test
    public void testCreateConnectionBindResponse() {
        // Given:
        // When:
        Response connectionBindResponse = MessageFactory.createConnectionBindResponse();
        // Then:
        assertNotNull(connectionBindResponse);
        assertEquals(Message.CONNECTION_BIND_SUCCESS_RESPONSE, connectionBindResponse.getMessageType());
    }

    @Test
    public void testCreateConnectionBindErrorResponse() {
        // Given:
        char errorCode = '1';
        // When:
        Response connectionBindErrorResponse = MessageFactory.createConnectionBindErrorResponse(errorCode);
        // Then:
        assertNotNull(connectionBindErrorResponse);
        assertEquals(Message.CONNECTION_BIND_ERROR_RESPONSE, connectionBindErrorResponse.getMessageType());
        // Verify error code attribute is added
        Attribute errorCodeAttribute = connectionBindErrorResponse.getAttribute(Attribute.ERROR_CODE_ATTRIBUTE);
        assertNotNull(errorCodeAttribute);
        assertEquals(errorCode, ((org.ice4j.attribute.ErrorCodeAttribute) errorCodeAttribute).getErrorCode());
    }

    @Test
    public void testCreateConnectionAttemptIndication() {
        // Given:
        int connectionIdValue = 10;
        TransportAddress peerAddress = transportAddress;
        // When:
        Indication connectionAttemptIndication = MessageFactory.createConnectionAttemptIndication(connectionIdValue, peerAddress);
        // Then:
        assertNotNull(connectionAttemptIndication);
        assertEquals(Message.CONNECTION_ATTEMPT_INDICATION, connectionAttemptIndication.getMessageType());
        //
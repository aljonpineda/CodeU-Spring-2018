package codeu.model.store.basic;

import codeu.model.data.Message;
import codeu.model.store.persistence.PersistentStorageAgent;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;


public class MessageStoreTest {

  private MessageStore messageStore;
  private PersistentStorageAgent mockPersistentStorageAgent;

  private final UUID CONVERSATION_ID_ONE = UUID.randomUUID();
  private final Message MESSAGE_ONE =
      new Message(
          UUID.randomUUID(),
          CONVERSATION_ID_ONE,
          UUID.randomUUID(),
          "message one",
          Instant.ofEpochMilli(1000));
  private final Message MESSAGE_TWO =
      new Message(
          UUID.randomUUID(),
          CONVERSATION_ID_ONE,
          UUID.randomUUID(),
          "message two",
          Instant.ofEpochMilli(2000));
  private final Message MESSAGE_THREE =
      new Message(
          UUID.randomUUID(),
          UUID.randomUUID(),
          UUID.randomUUID(),
          "message three",
          Instant.ofEpochMilli(3000));

  @Before
  public void setup() {
    mockPersistentStorageAgent = Mockito.mock(PersistentStorageAgent.class);
    messageStore = MessageStore.getTestInstance(mockPersistentStorageAgent);

    final List<Message> messageList = new ArrayList<>();
    messageList.add(MESSAGE_ONE);
    messageList.add(MESSAGE_TWO);
    messageList.add(MESSAGE_THREE);
    messageStore.setMessages(messageList);
  }

  @Test
  public void testGetMessagesInConversation() {
    List<Message> resultMessages = messageStore.getMessagesInConversation(CONVERSATION_ID_ONE);

    Assert.assertEquals(2, resultMessages.size());
    assertEquals(MESSAGE_ONE, resultMessages.get(0));
    assertEquals(MESSAGE_TWO, resultMessages.get(1));
  }

  @Test
  public void testGetMessageById_found() {
    Message resultMessage = messageStore.getMessageById(MESSAGE_ONE.getId());

    Assert.assertEquals(MESSAGE_ONE, resultMessage);
  }

  @Test
  public void testGetMessageById_notFound() {
    Message randomMessage = messageStore.getMessageById(UUID.randomUUID());

    Assert.assertNull(randomMessage);
  }

  @Test
  public void testAddMessage() {
    UUID inputConversationId = UUID.randomUUID();
    Message inputMessage =
        new Message(
            UUID.randomUUID(),
            inputConversationId,
            UUID.randomUUID(),
            "test message",
            Instant.now());

    messageStore.addMessage(inputMessage);
    Message resultMessage = messageStore.getMessagesInConversation(inputConversationId).get(0);

    assertEquals(inputMessage, resultMessage);
    Mockito.verify(mockPersistentStorageAgent).writeThrough(inputMessage);
  }

  @Test
  public void testCountMessages(){
    UUID inputConversationId = UUID.randomUUID();
    Message inputMessage =
        new Message(
            UUID.randomUUID(),
            inputConversationId,
            UUID.randomUUID(),
            "test message",
            Instant.now());

    messageStore.addMessage(inputMessage);

    Assert.assertEquals(4, messageStore.countTotalMessages());
  }

  @Test
  public void testLastMessage(){
    assertEquals(MESSAGE_THREE, messageStore.getLastMessageIndex());
  }

  private void assertEquals(Message expectedMessage, Message actualMessage) {
    Assert.assertEquals(expectedMessage.getId(), actualMessage.getId());
    Assert.assertEquals(expectedMessage.getConversationId(), actualMessage.getConversationId());
    Assert.assertEquals(expectedMessage.getAuthorId(), actualMessage.getAuthorId());
    Assert.assertEquals(expectedMessage.getContent(), actualMessage.getContent());
    Assert.assertEquals(expectedMessage.getCreationTime(), actualMessage.getCreationTime());
  }

  @Test
  public void testDeleteMessage() {
    UUID messageId = UUID.randomUUID();
    Message inputMessage =
        new Message(
            messageId,
            UUID.randomUUID(),
            UUID.randomUUID(),
            "test message",
            Instant.now());

    messageStore.addMessage(inputMessage);
    messageStore.deleteMessage(inputMessage);
    Assert.assertEquals(null, messageStore.getMessageById(messageId));
    Mockito.verify(mockPersistentStorageAgent).deleteMessageThrough(inputMessage);
  }
}

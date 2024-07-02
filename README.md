# java-filmorate

Template repository for Filmorate project.

### Diagram of filmorate database.

![Screenshot of the diagram of database.](/src/main/resources/diagram.png)

### request of get films

- **SELECT** *
  
  **FROM** films;

### request of get users

- **SELECT** *
  
  **FROM** users

### request of get top-3 popular film

- **SELECT** f.id COUNT(l.user_id) AS like_count  
  **FROM** films f 
  
  **JOIN** likes l **ON** f.id = l.film_id  
  **GROUP BY** f.id, f.name
  
  **ORDER BY** like_count **DESC**
  
  **LIMIT** 3;

### request of get friends of user

- **SELECT** friend_id **AS** friend  
  **FROM** friends 
  
  **WHERE** user_id = ?  
  **AND** status = true;

### request of get mutual friends of two user

- **SELECT** f1.friend_id **AS** friend
  
  **FROM** friends f1
  
  **JOIN** friends f2 **ON** f1.friend_id = f2.friend_id
  
  **WHERE** f1.user_id = ?
  
  **AND** f1.status = true
  
  **AND** f2.user_id = ?
  
  **AND** f2.status = true;
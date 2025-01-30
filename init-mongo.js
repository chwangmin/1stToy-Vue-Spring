// init-mongo.js
db = db.getSiblingDB('admin');
db.auth('root', 'rootpassword');

db = db.getSiblingDB('board');

db.createUser({
  user: 'test',
  pwd: 'test',
  roles: [
    { role: 'admin', db: 'board' }
  ]
});

db.createCollection('board');
db.createCollection('member');

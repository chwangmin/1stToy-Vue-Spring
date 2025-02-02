db = db.getSiblingDB('admin');
db.auth('root', 'rootpassword');

// admin DB에 직접 사용자 생성
db.createUser({
  user: 'test',
  pwd: 'testpassword1234',
  roles: [
    { role: 'readWrite', db: 'board' },
    { role: 'dbAdmin', db: 'board' }
  ]
});

// 이후 board DB 생성
db = db.getSiblingDB('board');
db.createCollection('board');
db.createCollection('member');
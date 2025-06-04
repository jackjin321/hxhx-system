

mysqldump -uroot -pCj0dKF,wfKhua --all-databases >/home/work/all.sql

mysqldump -uroot -proot --databases db1 db2 >/tmp/user.sql


mysqldump -uroot -pCj0dKF,wfKhua --databases hxhx >/home/work/hxhx.sql


==> redis@6.2
redis@6.2 is keg-only, which means it was not symlinked into /usr/local,
because this is an alternate version of another formula.

If you need to have redis@6.2 first in your PATH, run:
  echo 'export PATH="/usr/local/opt/redis@6.2/bin:$PATH"' >> ~/.zshrc

To start redis@6.2 now and restart at login:
  brew services start redis@6.2
Or, if you don't want/need a background service you can just run:
  /usr/local/opt/redis@6.2/bin/redis-server /usr/local/etc/redis.conf
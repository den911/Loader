box.schema.user.create('test', { password = 'test' })
box.schema.user.grant('test', 'execute,read,write', 'universe')

-- docker run --name tarantool -d -p 3301:3301 -v /Users/ddyakin/Documents/tarantool/data:/var/lib/tarantool tarantool/tarantool:1.7
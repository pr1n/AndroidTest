package com.pr1n.repository.remote.base

class Get(lambda: INoBodyParamBuilder.() -> Unit) : Method(lambda)
class Head(lambda: INoBodyParamBuilder.() -> Unit) : Method(lambda)
class Post(lambda: IParamBuilder.() -> Unit) : Method(lambda)
class Put(lambda: IParamBuilder.() -> Unit) : Method(lambda)
class Delete(lambda: IParamBuilder.() -> Unit) : Method(lambda)
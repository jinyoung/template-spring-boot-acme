forEach: Relation
fileName: {{target.namePascalCase}}Service.java
path: {{source.boundedContext.name}}/{{options.packagePath}}/external
except: {{contexts.except}}
ifDuplicated: merge
---

package {{options.package}}.external;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Date;
import java.util.List;


{{#if value.fallback}}
@FeignClient(name = "{{target.boundedContext.name}}", url = "{{apiVariable target.boundedContext.name}}", fallback = {{target.aggregate.namePascalCase}}ServiceImpl.class)
{{else}}
@FeignClient(name = "{{target.boundedContext.name}}", url = "{{apiVariable target.boundedContext.name}}")
{{/if}}
 
{{#target}}
public interface {{namePascalCase}}Service {
    @GetMapping(path="/{{namePlural}}")
    public List<{{namePascalCase}}> get{{namePascalCase}}();

    @GetMapping(path="/{{namePlural}}/{id}")
    public {{namePascalCase}} get{{namePascalCase}}(@PathVariable("id") {{keyFieldDescriptor.className}} id);
}
{{/target}}



<function>
 

    let isGetInvocation = ((this.source._type.endsWith("Command") || this.source._type.endsWith("Policy")) && ((this.target._type.endsWith("View") && this.target.dataProjection=="cqrs") || this.target._type.endsWith("Aggregate")))
    let isPostInvcation = ((this.source._type.endsWith("Event") || this.source._type.endsWith("Policy")) && this.target._type.endsWith("Command"))
    let isExternalInvocation = (this.source.boundedContext.name != this.target.boundedContext.name)

    this.contexts.except = !(isExternalInvocation && isGetInvocation)
 
if(!this.contexts.except){
 
}
 
</function>
forEach: Relation
fileName: {{target.aggregate.namePascalCase}}ServiceImpl.java
path: {{source.boundedContext.name}}/{{options.packagePath}}/external
except: {{contexts.except}}
ifDuplicated: merge
---

package {{options.package}}.external;

import org.springframework.stereotype.Service;

//<<< Resilency / Fallback 
{{#ifContains "$.target._type" "View"}}
{{#ifEquals target.dataProjection "query-for-aggregate"}}
public class {{target.aggregate.namePascalCase}}ServiceImpl implements {{target.aggregate.namePascalCase}}Service {
    public {{target.aggregate.namePascalCase}} {{camelCase target.name}}({{pascalCase target.name}}Query query){}
{{/ifEquals}}
{{/ifContains}}
}

//>>> Resilency / Fallback


<function> 
 
this.contexts.except = !(this.value.fallback && (this.source._type.endsWith("Command") || this.source._type.endsWith("Policy")) && (this.target._type.endsWith("View") || this.target._type.endsWith("Aggregate")))
 
if(!this.contexts.except){
 
}
 
</function>
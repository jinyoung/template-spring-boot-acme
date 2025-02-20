forEach: Aggregate
representativeFor: Aggregate
fileName: {{namePascalCase}}.java
path: {{boundedContext.name}}/{{{options.packagePath}}}/domain
---
package {{options.package}}.domain;

{{#lifeCycles}}
{{#events}}
import {{../../options.package}}.domain.{{namePascalCase}};
{{/events}}
{{/lifeCycles}}
import {{options.package}}.{{boundedContext.namePascalCase}}Application;
import javax.persistence.*;
import java.util.List;
import lombok.Data;
import java.util.Date;
{{#checkBigDecimal aggregateRoot.fieldDescriptors}}{{/checkBigDecimal}}

@Entity
@Table(name="{{namePascalCase}}_table")
@Data
{{#setDiscriminator aggregateRoot.entities.relations nameCamelCase}}{{/setDiscriminator}}
//<<< DDD / Aggregate Root
public class {{namePascalCase}} {{#checkExtends aggregateRoot.entities.relations namePascalCase}}{{/checkExtends}} {


    {{#aggregateRoot.fieldDescriptors}}
    {{^isVO}}{{#isKey}}
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    {{/isKey}}{{/isVO}}
    {{#isLob}}@Lob{{/isLob}}
    {{#if (isPrimitive className)}}{{#isList}}@ElementCollection{{/isList}}{{/if}}
    {{#checkRelations ../aggregateRoot.entities.relations className isVO referenceClass isList}}{{/checkRelations}}
    private {{{className}}} {{nameCamelCase}};
    {{/aggregateRoot.fieldDescriptors}}

{{#lifeCycles}}
    {{annotation}}
    public void on{{trigger}}(){
    {{#events}}

        {{#relationCommandInfo}}
            {{#commandValue}}
        //Following code causes dependency to external APIs
        // it is NOT A GOOD PRACTICE. instead, Event-Policy mapping is recommended.

        {{^isRestRepository}}
        {{#if (has fieldDescriptors)}}
        {{../../../../options.package}}.external.{{namePascalCase}}Command {{nameCamelCase}}Command = new {{../../../../options.package}}.external.{{namePascalCase}}Command();
        // mappings goes here
        {{../boundedContext.namePascalCase}}Application.applicationContext.getBean({{../../../../options.package}}.external.{{aggregate.namePascalCase}}Service.class)
            .{{nameCamelCase}}(/* get???(), */ {{nameCamelCase}}Command);
        {{/if}}
        {{/isRestRepository}}

        {{#isRestRepository}}
        {{../../../../options.package}}.external.{{aggregate.namePascalCase}} {{aggregate.nameCamelCase}} = new {{../../../../options.package}}.external.{{aggregate.namePascalCase}}();
        // mappings goes here
        {{../boundedContext.namePascalCase}}Application.applicationContext.getBean({{../../../../options.package}}.external.{{aggregate.namePascalCase}}Service.class)
            .{{nameCamelCase}}({{aggregate.nameCamelCase}});
        {{/isRestRepository}}

            {{/commandValue}}
        {{/relationCommandInfo}}

        {{namePascalCase}} {{nameCamelCase}} = new {{namePascalCase}}(this);
        {{nameCamelCase}}.publishAfterCommit();

    {{/events}}
    {{#commands}}
        {{#relationCommandInfo}}
            {{#commandValue}}
        // Get request from {{aggregate.namePascalCase}}
        //{{../../../../options.package}}.external.{{aggregate.namePascalCase}} {{aggregate.nameCamelCase}} =
        //    {{../relationCommandInfo.boundedContext.namePascalCase}}Application.applicationContext.getBean({{../../../../options.package}}.external.{{aggregate.namePascalCase}}Service.class)
        //    .get{{aggregate.namePascalCase}}(/** mapping value needed */);

            {{/commandValue}}
        {{/relationCommandInfo}}
    {{/commands}}
    }
{{/lifeCycles}}

    public static {{namePascalCase}}Repository repository(){
        {{namePascalCase}}Repository {{nameCamelCase}}Repository = {{boundedContext.namePascalCase}}Application.applicationContext.getBean({{namePascalCase}}Repository.class);
        return {{nameCamelCase}}Repository;
    }

{{#aggregateRoot.operations}}
    {{#setOperations ../commands name}}
    {{#isOverride}}
    @Override
    {{/isOverride}}
    {{^isRootMethod}}
    public {{returnType}} {{name}}(){
        //
    }
    {{/isRootMethod}}
    {{/setOperations}}
{{/aggregateRoot.operations}}


    {{#commands}}
    {{^isRestRepository}}
//<<< Clean Arch / Port Method
    public void {{nameCamelCase}}({{#if (has fieldDescriptors)}}{{namePascalCase}}Command {{nameCamelCase}}Command{{/if}}){
        {{#triggerByCommand}}
        {{eventValue.namePascalCase}} {{eventValue.nameCamelCase}} = new {{eventValue.namePascalCase}}(this);
        {{eventValue.nameCamelCase}}.publishAfterCommit();

        {{#relationCommandInfo}}
        {{#commandValue}}
        //Following code causes dependency to external APIs
        // it is NOT A GOOD PRACTICE. instead, Event-Policy mapping is recommended.

        {{../../../../options.package}}.external.{{aggregate.namePascalCase}} {{aggregate.nameCamelCase}} = new {{../../../../options.package}}.external.{{aggregate.namePascalCase}}();
        // mappings goes here
        {{../boundedContext.namePascalCase}}Application.applicationContext.getBean({{../../../../options.package}}.external.{{aggregate.namePascalCase}}Service.class)
            .{{nameCamelCase}}({{aggregate.nameCamelCase}});

        {{/commandValue}}
        {{/relationCommandInfo}}
        {{/triggerByCommand}}
    }
//>>> Clean Arch / Port Method
    {{/isRestRepository}}
    {{/commands}}

    {{#policyList}}
    {{#relationEventInfo}}
//<<< Clean Arch / Port Method
    public static void {{../nameCamelCase}}({{eventValue.namePascalCase}} {{eventValue.nameCamelCase}}){

        /** Example 1:  new item 
        {{../../namePascalCase}} {{../../nameCamelCase}} = new {{../../namePascalCase}}();
        repository().save({{../../nameCamelCase}});

        {{#../relationExampleEventInfo}}
        {{eventValue.namePascalCase}} {{eventValue.nameCamelCase}} = new {{eventValue.namePascalCase}}({{../../../nameCamelCase}});
        {{eventValue.nameCamelCase}}.publishAfterCommit();
        {{/../relationExampleEventInfo}}
        */

        /** Example 2:  finding and process
        
        repository().findById({{eventValue.nameCamelCase}}.get???()).ifPresent({{../../nameCamelCase}}->{
            
            {{../../nameCamelCase}} // do something
            repository().save({{../../nameCamelCase}});

            {{#../relationExampleEventInfo}}
            {{eventValue.namePascalCase}} {{eventValue.nameCamelCase}} = new {{eventValue.namePascalCase}}({{../../../nameCamelCase}});
            {{eventValue.nameCamelCase}}.publishAfterCommit();
            {{/../relationExampleEventInfo}}

         });
        */

        
    }
//>>> Clean Arch / Port Method
    {{/relationEventInfo}}
    {{/policyList}}


}
//>>> DDD / Aggregate Root

<function>
window.$HandleBars.registerHelper('checkDateType', function (fieldDescriptors) {
    for(var i = 0; i < fieldDescriptors.length; i ++ ){
        if(fieldDescriptors[i] && fieldDescriptors[i].className == 'Date'){
        return "import java.util.Date; \n"
        }
    }
});

window.$HandleBars.registerHelper('checkBigDecimal', function (fieldDescriptors) {
    for(var i = 0; i < fieldDescriptors.length; i ++ ){
        if(fieldDescriptors[i] && fieldDescriptors[i].className.includes('BigDecimal')){
            return "import java.math.BigDecimal;";
        }
    }
});


window.$HandleBars.registerHelper('isPrimitive', function (className) {
    if(className.includes("String") || className.includes("Integer") || className.includes("Long") || className.includes("Double") || className.includes("Float")
            || className.includes("Boolean") || className.includes("Date")){
        return true;
    } else {
        return false;
    }
});

window.$HandleBars.registerHelper('checkRelations', function (relations, className, isVO, referenceClass, isList) {
    try {
        if(typeof relations === "undefined") {
            return 
        } else {
            // ValueObject
            if(isVO) {
                if(isList) {
                    return "@ElementCollection"
                } else {
                    return "@Embedded"
                }
            } else {
                for(var i = 0; i < relations.length; i ++ ) {
                    if(relations[i] != null) {
                        if(className.includes(relations[i].targetElement.name) && !relations[i].relationType.includes("Generalization")) {
                            // Enumeration
                            if(relations[i].targetElement._type.endsWith('enum') || relations[i].targetElement._type.endsWith('Exception')) {
                                return
                            }
                            // complex type
                            if(relations[i].sourceMultiplicity == "1" &&
                                    (relations[i].targetMultiplicity == "1..n" || relations[i].targetMultiplicity == "0..n") || className.includes("List")
                            ) {
                                return "@OneToMany"

                            } else if((relations[i].sourceMultiplicity == "1..n" || relations[i].sourceMultiplicity == "0..n") && relations[i].targetMultiplicity == "1"){
                                return "@ManyToOne"
                            
                            } else if(relations[i].sourceMultiplicity == "1" && relations[i].targetMultiplicity == "1"){
                                return "@OneToOne"
                            
                            } else if((relations[i].sourceMultiplicity == "1..n" || relations[i].sourceMultiplicity == "0..n") &&
                                    (relations[i].targetMultiplicity == "1..n" || relations[i].targetMultiplicity == "0..n") || className.includes("List")
                            ) {
                                return "@ManyToMany"
                            }
                        }
                    }
                }
                if(referenceClass) {
                    return "@OneToOne"
                }
            }
        }
    } catch (e) {
        console.log(e)
    }
});

window.$HandleBars.registerHelper('checkExtends', function (relations, name) {
    try {
        if(typeof relations === "undefined" || name === "undefined"){
            return;
        } else {
            for(var i = 0; i < relations.length; i ++ ){
                if(relations[i] != null){
                    if(relations[i].sourceElement.name == name && relations[i].relationType.includes("Generalization")){
                        var text = "extends " + relations[i].targetElement.name
                        return text
                    }
                }
            }
        }
    } catch(e) {
        console.log(e)
    }
});

window.$HandleBars.registerHelper('setDiscriminator', function (relations, name) {
    try {
        if (typeof relations == "undefined") {
            return 
        } else {
            for (var i = 0; i < relations.length; i ++ ) {
                if (relations[i] != null) {
                    var text = ''
                    if (relations[i].targetElement != "undefined") {
                        if(relations[i].targetElement.name.toLowerCase() == name && relations[i].relationType.includes("Generalization")) {
                            text = '@DiscriminatorColumn(\n' + 
                                '    discriminatorType = DiscriminatorType.STRING,\n' +
                                '    name = "' + name + '_type",\n' +
                                '    columnDefinition = "CHAR(5)"\n' +
                                ')'
                            return text
                        }
                    } else {
                        if(relations[i].toName.toLowerCase() == name && relations[i].relationType.includes("Generalization")) {
                            text = '@DiscriminatorColumn(\n' + 
                                '    discriminatorType = DiscriminatorType.STRING,\n' +
                                '    name = "' + name + '_type",\n' +
                                '    columnDefinition = "CHAR(5)"\n' +
                                ')'
                            return text
                        }
                    }
                    if (relations[i].sourceElement != "undefined") {
                        if (relations[i].sourceElement.name.toLowerCase() == name && relations[i].relationType.includes("Generalization")) {
                            return '@DiscriminatorValue("' + name + '")'
                        }
                    } else {
                        if (relations[i].fromName.toLowerCase() == name && relations[i].relationType.includes("Generalization")) {
                            return '@DiscriminatorValue("' + name + '")'
                        }
                    }
                }
            }
        }
    } catch(e) {
        console.log(e)
    }
});

window.$HandleBars.registerHelper('setOperations', function (commands, name, options) {
    try {
        if(commands == "undefined") {
            return options.fn(this);
        }
        var isCmd = false;
        for (var i = 0; i < commands.length; i ++ ) {
            if(commands[i] != null) {
                if (commands[i].name == name && commands[i].isRestRepository != true) {
                    isCmd = true
                }
            }
        }
        if(isCmd) {
            return options.inverse(this);
        } else {
            return options.fn(this);
        }
    } catch(e) {
        console.log(e)
    }
});


window.$HandleBars.registerHelper('has', function (members) {
    try {
        return (members.length > 0);
    } catch(e) {
        console.log(e)
    }
});


</function>

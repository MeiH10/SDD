# Database Specifications

This documented describes the type of data that is stored in our MongoDB
cluster.

## Account

```json
{
  "id": "String",
  "rscid": "String",
  "created": "Date",
  "username": "String",
  "password": "encrypt<String>",
  "likes": "set<ref<Resource>>",
  "dislike": "set<ref<Resource>>"
}
```

## Semester

```json
{
  "id": "String",
  "year": "Number",
  "type": "'fall' | 'spring' | 'summer'"
}
```

## Major

```json
{
  "id": "String",
  "department": "String",
  "code": "String",
  "name": "String",
}
```

## Class

```json
{
  "id": "String",
  "major": "ref<Major>",
  "semester": "ref<Semester>",
  "code": "String",
}
```

## Section

```json
{
  "id": "String",
  "class": "ref<Class>",
  "professors": "List<String>",
  "section": "Number",
}
```

## Note

```json
{
  "id": "String",
  "name": "String",
  "description?": "String",
  "created": "Date",
  "type": "'link' | 'crib-sheet' | 'notes' | ''",
  "file": {
    "type": "'pdf' | 'png' | 'webp' | 'docx' | 'mp3'",
    "path": "String",
    "internal": "Boolean"
  },
  "section": "ref<Section>",
  "likes": "set<ref<Account>>",
  "dislikes": "set<ref<Account>>",
}
```

## Report

```json
{
  "id": "String",
  "type": "'note' | 'comment'",
  "note": "ref<Note>",
  "comment": "ref<Comment>"
}
```

## Comment

```json
{
  "id": "String",
  "comment": "String",
  "created": "Date",
  "account": "ref<Account>",
  "note": "ref<Note>",
  "likes": "set<ref<Account>>",
  "dislikes": "set<ref<Account>>",
  "flagged": "Boolean"
}
```

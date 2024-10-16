---
marp: true
---
<!--
theme: gaia
size: 16:9
paginate: true
author: 'Dylan Langumier and Raphael Perret' 
title: 'BIM'
description: 'Basic Image Manipulation'
url: https://github.com/Dylan-Langumier/DAI-practical-work-1/presentation.md
footer: '**HEIG-VD** - DAI Course 2024-2025'
style: |
    :root {
        --color-background: #fff;
        --color-foreground: #333;
        --color-highlight: #f96;
        --color-dimmed: #888;
        --color-headings: #7d8ca3;
    }
    blockquote {
        font-style: italic;
    }
    table {
        width: 100%;
    }
    th:first-child {
        width: 15%;
    }
    h1, h2, h3, h4, h5, h6 {
        color: var(--color-headings);
    }
    h2, h3, h4, h5, h6 {
        font-size: 1.5rem;
    }
    h1 a:link, h2 a:link, h3 a:link, h4 a:link, h5 a:link, h6 a:link {
        text-decoration: none;
    }
    section:not([class=lead]) > p, blockquote {
        text-align: justify;
    }
headingDivider: 4
-->

[base]:test_images/desert.bmp
[grey]:test_images/grey_desert.bmp
[colorful]:test_images/desert_200_color.bmp
[red]:test_images/desert_red.bmp
[sepia]:test_images/sepia_desert.bmp

# Presentation of DAI project : BIM
<!--
_class: lead
_paginate: false
-->

![bg opacity:10%][grey]

Dylan Langumier & Raphael Perret

## Our project

- works with simple bmp files
- applies a variety of filters with many parameters
- simple CLI tool

![bg right:40%][colorful]

## Example 1
```
java -jar target/DAI-filters-1.0-SNAPSHOT.jar
 -f COLOR_INTENSITY test_images/desert.bmp 
 test_images/desert_200_color.bmp 200 apply
```

**Filter** : COLOR_INTENSITY  

**Intensity** : 200%  

![bg vertical][base]
![bg right:60%][colorful]

## Example 2
Two filters applied:
1. **Adjust color** 150 20 20 250  
2. **Moving average**, size 2  
![bg vertical][base]
![bg right:60%][red]

## Code
- commands : picocli stuff
- file : image file read/write
- filters
- image : the simple representation used troughout our project

file and filter are actually interfaces with different implementations. This is redundant for file image since we only implemented bitmap.

![bg right:40%][sepia]
package com.lixp.exam.service;

import com.lixp.exam.bean.CardNote;
import com.lixp.exam.dao.CardNoteMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class CardNoteService {

    @Autowired
    CardNoteMapper cardNoteMapper;

    /**
     * 查询提示信息
     * @return
     */
    public CardNote selectAllCardNote(){
        CardNote cardNote=new CardNote();
        cardNote.setcKey("提示信息");
        return cardNoteMapper.selectOne(cardNote);
    }

    /**
     * 修改提示信息
     * @return
     */
    public boolean saveCardNote(String cValue){
        Example example=new Example(CardNote.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("cKey","提示信息");
        CardNote cardNote=new CardNote();
        cardNote.setcValue(cValue);
        int i = cardNoteMapper.updateByExampleSelective(cardNote, example);

        return i>0;
    }

}

import type {CareLevelHistory} from "../../types/loc.type";


interface Props{
    data:CareLevelHistory[]
}


export default function AdlBreakdownTable({
    data
}:Props){


return (

<div className="
mt-8
bg-white
rounded-2xl
border
overflow-hidden
">


<table className="w-full">


<thead>

<tr className="bg-gray-50">

<th className="p-4 text-left">
Level
</th>


<th className="p-4">
Start Date
</th>


<th className="p-4">
End Date
</th>


</tr>

</thead>



<tbody>


{
data.map(item=>(

<tr
key={item.id}
className="border-t"
>


<td className="p-4">

{item.levelCode}

</td>


<td className="p-4 text-center">

{item.startDate}

</td>


<td className="p-4 text-center">

{
item.endDate ?? "Current"
}

</td>


</tr>


))
}


</tbody>


</table>


</div>


)

}